import { Component } from '@angular/core';

@Component({
  selector: 'chessdb-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {

  constructor() {
  }

  SETTINGS_VISIBLE: 'GENERIC' | 'DISPLAY' | 'ACCOUNT' = 'GENERIC';

}
