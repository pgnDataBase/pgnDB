import {Component, EventEmitter, Input, Output} from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import {ServerService} from '../../../services/backend-communication/server.service';
import {ModalMessageService} from '../../common/modal-message/modal-message.service';


@Component({
  selector: 'file-uploader',
  templateUrl: './file-uploader.component.html',
  styleUrls: ['./file-uploader.component.scss']
})
export class FileUploaderComponent {

  form: FormGroup;
  fileToUpload: File;
  @Input() dbName: string;
  loading: boolean = false;

  constructor(private fb: FormBuilder,
              private server: ServerService,
              private modalMessage: ModalMessageService) {
  }

  ngOnInit() {
    this.formInitialization();
  }

  formInitialization() {
    this.form = this.fb.group({
      fileUpload: null
    });
  }

  fileChange(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      this.fileToUpload = event.target.files[0];
    }
  }

  async uploadPgnFile() {
    let body = new FormData();
    body.append("file", this.fileToUpload);
    this.loading = true;
    await this.server.uploadPngFile(body, this.dbName).then((res) => {
      this.modalMessage.openModalMessage('File was uploaded');
      this.server.downloadAndRefreshDatabases();
    }).catch((err) => {
      this.modalMessage.openModalMessageOnChessDBException(err.error);
    });
    this.loading = false;
  }
}
