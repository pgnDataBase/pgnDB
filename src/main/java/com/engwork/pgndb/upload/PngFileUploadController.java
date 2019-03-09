package com.engwork.pgndb.upload;

import com.engwork.pgndb.accessauthorization.AccessAuthorizationBean;
import com.engwork.pgndb.accessauthorization.AccessExceptionsFactory;
import com.engwork.pgndb.security.JWTValidator;
import com.engwork.pgndb.statusresolver.StatusResolver;
import com.engwork.pgndb.treebuilding.treestatus.TreeStatusBean;
import com.engwork.pgndb.upload.exceptions.ParallelLoadingToOneDatabaseException;
import com.engwork.pgndb.users.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class PngFileUploadController {

  private final ChessGamesToDbLoaderBean chessGamesToDbLoaderBean;
  private final TreeStatusBean treeStatusBean;
  private final StatusResolver loadingStatusResolver;

  private final AccessAuthorizationBean accessAuthorizationBean;
  private final AccessExceptionsFactory accessExceptionsFactory;
  private final JWTValidator validator;

  @Autowired
  public PngFileUploadController(ChessGamesToDbLoaderBean chessGamesToDbLoaderBean,
                                 TreeStatusBean treeStatusBean,
                                 @Qualifier("loading.status.resolver") StatusResolver loadingStatusResolver,
                                 AccessAuthorizationBean accessAuthorizationBean,
                                 AccessExceptionsFactory accessExceptionsFactory,
                                 JWTValidator validator
  ) {
    this.chessGamesToDbLoaderBean = chessGamesToDbLoaderBean;
    this.treeStatusBean = treeStatusBean;
    this.loadingStatusResolver = loadingStatusResolver;
    this.accessAuthorizationBean = accessAuthorizationBean;
    this.accessExceptionsFactory = accessExceptionsFactory;
    this.validator = validator;
  }

  @PostMapping("/upload/{dbName}")
  public ResponseEntity singleFileUpload(@RequestParam("file") MultipartFile file,
                                         @PathVariable("dbName") String dbName,
                                         @RequestHeader("Authorization") String token) {
    token = token.replace("Bearer ", "");
    User user = validator.validate(token);
    if (!accessAuthorizationBean.validateAccess(user.getId(), dbName)) {
      throw accessExceptionsFactory.noAccessWithName(dbName);
    }

    log.info("Entered WEB file upload for db: {}.", dbName);
    if (file.isEmpty()) {
      ResponseEntity.badRequest();
    }
    try {
      File convertedFile = this.convertMultipartFileToFile(file);
      if (loadingStatusResolver.getStatusByKey(dbName) == null) {
        this.chessGamesToDbLoaderBean.loadGamesToDatabase(convertedFile, dbName);
      } else {
        log.warn("Parallel loading not allowed to database {}!", dbName);
        if (convertedFile.isFile()) {
          FileUtils.forceDelete(convertedFile);
        }
        throw new ParallelLoadingToOneDatabaseException(dbName);
      }
      log.info("File successfully uploaded through WEB to db: {}.", dbName);
      if (convertedFile.isFile()) {
        FileUtils.forceDelete(convertedFile);
      }
      treeStatusBean.setTreeNotUpToDate(dbName);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ResponseEntity.ok().body(null);
  }

  private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
    File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
    FileOutputStream fos = new FileOutputStream(convertedFile);
    fos.write(multipartFile.getBytes());
    fos.close();
    return convertedFile;
  }
}
