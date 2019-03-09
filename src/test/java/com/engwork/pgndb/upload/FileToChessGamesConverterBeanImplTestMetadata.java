package com.engwork.pgndb.upload;

import com.engwork.pgndb.chessgamesconverter.ChessGamesConverterInjector;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameData;
import com.engwork.pgndb.chessgamesconverter.model.ChessGameMetadata;
import com.engwork.pgndb.chessgamesconverter.model.Move;
import com.engwork.pgndb.chessgamesconverter.model.Position;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({UploadingInjector.class, ChessGamesConverterInjector.class})
@WebAppConfiguration
public class FileToChessGamesConverterBeanImplTestMetadata {

  private static final String RESOURCES_PATH = "src/test/resources/".replace("/", File.separator);

  private List<ChessGameData> games;
  private boolean testInit = false;
  private int gameIndex;

  @Before
  public void setUp() throws FileNotFoundException {
    if (!testInit) {
      File file = ResourceUtils.getFile(RESOURCES_PATH + "MacKenzie.pgn");
      games = this.fileToChessGamesConverterBean.getChessGamesFromFile(file);
      testInit = true;
    }
    Random generator = new Random();
    gameIndex = generator.nextInt(games.size() - 1);
  }

  @Test(expected = FileNotFoundException.class)
  public void shouldThrowExceptionOnBadFile() throws FileNotFoundException {
    this.fileToChessGamesConverterBean.getChessGamesFromFile(new File("alolalol"));
  }

  @Test
  public void shouldConvertExampleFileToAppropriateSizeList() {
    assertThat(games.size()).isEqualTo(19);
  }

  @Test
  public void checkIfMoveNumberInSecondGameIsValid() {
    assertThat(games.get(1).getMoveList().size()).isEqualTo(47);
  }

  @Test
  public void checkIfPieceCodeIsValid() {
    List<Move> moveList = games.get(gameIndex).getMoveList();
    Predicate<Move> predicate = move -> (move.getPieceCode().length() == 3);
    assertThat(moveList).allMatch(predicate);
  }

  @Test
  public void checkIfCapturedPieceCodeIsValid() {
    List<Move> moveList = games.get(gameIndex).getMoveList();
    Predicate<Move> predicate = move -> move.getCapturedPieceCode() == null || move.getCapturedPieceCode().length() == 3;
    assertThat(moveList).allMatch(predicate);
  }

  @Test
  public void checkIfPromotedPieceCodeIsValid() {
    List<Move> moveList = games.get(gameIndex).getMoveList();
    Predicate<Move> predicate = move -> move.getPromotedPieceCode() == null || move.getPromotedPieceCode().length() == 3;
    assertThat(moveList).allMatch(predicate);
  }

  private static boolean isAttackingMove(Move move) {
    if (move.getMoveType().equals("ENPT") || move.getMoveType().equals("ATCK"))
      return true;
    else
      return false;
  }

  private static boolean checkIfPiecesHaveDiffrentColor(String pieceCode1, String pieceCode2) {
    if (pieceCode1.charAt(0) == pieceCode2.charAt(0)) {
      return false;
    }
    return true;
  }

  @Test
  public void checkIfMoveTypeValid() {
    List<Move> moveList = games.get(gameIndex).getMoveList();
    Predicate<Move> predicate = move -> (!isAttackingMove(move) && move.getCapturedPieceCode() == null) ||
        (isAttackingMove(move) && move.getCapturedPieceCode() != null);
    assertThat(moveList).allMatch(predicate);
  }

  @Test
  public void checkAttackingMoves() {
    List<Move> moveList = games.get(gameIndex).getMoveList();
    Predicate<Move> predicate = move -> (isAttackingMove(move)
        && checkIfPiecesHaveDiffrentColor(move.getPieceCode(), move.getCapturedPieceCode()))
        || (!isAttackingMove(move) && move.getCapturedPieceCode() == null);
    assertThat(moveList).allMatch(predicate);
  }

  @Test
  public void checkIfFieldsAreValid() {
    List<Move> moveList = games.get(gameIndex).getMoveList();
    Predicate<Move> predicate = move -> move.getFromField().length() == 2 && move.getToField().length() == 2;
    assertThat(moveList).allMatch(predicate);
  }

  public static boolean isValueInRange(Integer value, int from, int to) {
    if (value >= from && value <= to)
      return true;
    else
      return false;
  }

  @Test
  public void checkIfPostionsAreValid() {
    List<Position> positionList = games.get(gameIndex).getPositionList();
    Predicate<Position> predicate = position -> isValueInRange(position.getBlackPawnsCount(), 0, 8)
        && isValueInRange(position.getWhitePawnsCount(), 0, 8)
        && isValueInRange(position.getBlackPiecesCount(), 1, 16)
        && isValueInRange(position.getWhitePiecesCount(), 1, 16);
    assertThat(positionList).allMatch(predicate);
  }

  private static boolean checkIfFENFieldsNoCorrect(String fen) {
    String shortenedFen = fen.split(" ")[0];
    Integer totalFieldsCount = 0;
    String[] fenRows = shortenedFen.split("/");
    for (String fenRow : fenRows) {
      Integer rowFieldsCount = 0;
      for (Character field : fenRow.toCharArray()) {
        if (Character.isDigit(field)) {
          rowFieldsCount += Integer.parseInt(field.toString());
        } else {
          rowFieldsCount++;
        }
      }
      if (rowFieldsCount != 8)
        return false;
      totalFieldsCount += rowFieldsCount;
    }
    if (totalFieldsCount == 64)
      return true;
    else
      return false;
  }

  private boolean isFenValid(String fen) {
    //All time it is require to have both kings on board
    if (!fen.contains("k") || !fen.contains("K"))
      return false;
    if (StringUtils.countOccurrencesOf(fen, "/") != 7) {
      return false;
    }
    return checkIfFENFieldsNoCorrect(fen);
  }

  @Test
  public void checkIfPostionsFENisValid() {
    List<Position> positionList = games.get(gameIndex).getPositionList();
    Predicate<Position> predicate = position -> isFenValid(position.getFen());
    assertThat(positionList).allMatch(predicate);
  }

  @Test
  public void checkIfResultIsValid() {
    assertThat(games.get(1).getChessGameMetadata().getResult()).isEqualTo("1-0");
  }

  @Test
  public void checkIfMetadataIsValid() {
    ChessGameMetadata metadata = games.get(gameIndex).getChessGameMetadata();
    assertThat(metadata).hasNoNullFieldsOrPropertiesExcept("gameId");
  }

  @Autowired
  private FileToChessGamesConverterBean fileToChessGamesConverterBean;

}
