import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {LibloaderService} from '../services/library-related/libloader.service';
import {ChessboardViewerComponent} from './chessboard-viewer/chessboard-viewer.component';
import {LeftSidePanelComponent} from './left-side-panel/left-side-panel.component';
import {GamesListPanelComponent} from './games-list-panel/games-list-panel.component';
import {PgnRowDisplayComponent} from './games-list-panel/pgn-row-display/pgn-row-display.component';
import {RouterModule, Routes} from '@angular/router';
import {ChessboardService} from '../services/library-related/chessboard.service';
import {LoginScreenComponent} from './login-screen/login-screen.component';
import {AuthenticationService} from './authorization/authentication.service';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ButtonDirective} from '../directives/button.directive';
import {InputDirective} from '../directives/input.directive';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {ServerService} from '../services/backend-communication/server.service';
import {InputErrorListComponent} from './common/input-error-list/input-error-list.component';
import {ErrorCodePipe} from '../pipes/error-code.pipe';
import {GameHeadersViewerComponent} from './chessboard-viewer/game-headers-viewer/game-headers-viewer.component';
import {ROUTING_PATHS} from '../routing/routing-paths';
import {PaginationService} from '../services/pagination/pagination.service';
import {GameStateReactorService} from '../services/state-holders/game-state-reactor.service';
import {SettingsComponent} from './settings/settings.component';
import {LoadingWhenDirective} from '../directives/loading-when.directive';
import {InfoMessageComponent} from './common/info-message/info-message.component';
import {LoadingComponent} from './common/loading/loading.component';
import {PaginationChangerComponent} from './games-list-panel/pagination-changer/pagination-changer.component';
import {TrimPipe} from '../pipes/trim.pipe';
import {RoundButtonComponent} from './left-side-panel/round-button/round-button.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ModalMessageComponent} from './common/modal-message/modal-message.component';
import {ModalMessageService} from './common/modal-message/modal-message.service';
import {FileUploaderComponent} from './database-edit/pgn-file-uploader/file-uploader.component';
import {DatabaseEditComponent} from './database-edit/database-edit.component';
import {ActionDropdownComponent} from './common/action-dropdown/action-dropdown.component';
import {PlainRowDisplayComponent} from './games-list-panel/plain-row-display/plain-row-display.component';
import {GameViewerComponent} from './chessboard-viewer/game-viewer/game-viewer.component';
import {GameEntityViewerComponent} from './chessboard-viewer/game-viewer/game-entity-viewer/game-entity-viewer.component';
import {AboutScreenComponent} from './login-screen/about-screen/about-screen.component';
import {MoveActionPanelComponent} from './chessboard-viewer/move-action-panel/move-action-panel.component';
import {GameActionPanelComponent} from './chessboard-viewer/game-viewer/game-action-panel/game-action-panel.component';
import {ButtonWithActionConfirmationComponent} from './common/button-with-action-confirmation/button-with-action-confirmation.component';
import {DatabaseAdderComponent} from './left-side-panel/database-adder/database-adder.component';
import {FilterDbComponent} from './filter-db/filter-db.component';
import {CancelConfirmInputComponent} from './common/cancel-confirm-input/cancel-confirm-input.component';
import {MaxLengthValidatorDirective} from '../directives/max-length-validator.directive';
import {HeaderAndPositionFilterFormComponent} from './filter-db/header-and-position-filter-form/header-and-position-filter-form.component';
import {HeadersFilterComponent} from './filter-db/header-and-position-filter-form/headers-filter/headers-filter.component';
import {PositionsFilterComponent} from './filter-db/header-and-position-filter-form/positions-filter/positions-filter.component';
import {BooleanButtonsComponent} from './common/boolean-buttons/boolean-buttons.component';
import {GameDisplaySettingsComponent} from './settings/game-display-settings/game-display-settings.component';
import {MaxValueValidatorDirective} from '../directives/max-value-validator.directive';
import {MinValueValidatorDirective} from '../directives/min-value-validator.directive';
import { GenericSettingsComponent } from './settings/generic-settings/generic-settings.component';
import { HeaderAutocompleteInputComponent } from './filter-db/header-and-position-filter-form/headers-filter/header-autocomplete-input/header-autocomplete-input.component';
import { StatusBarComponent } from './common/status-bar/status-bar.component';
import { GameViewerBottomPanelComponent } from './chessboard-viewer/game-viewer/game-viewer-bottom-panel/game-viewer-bottom-panel.component';
import { GeneralRadioButtonsComponent } from './common/general-radio-buttons/general-radio-buttons.component';
import { RadioButtonComponent } from './common/general-radio-buttons/radio-button/radio-button.component';
import { TreeViewerComponent } from './chessboard-viewer/tree-viewer/tree-viewer.component';
import { PromotePieceComponent } from './chessboard-viewer/edition/piece-promotion/promote-piece/promote-piece.component';
import { DatabaseChooseComponent } from './chessboard-viewer/tree-viewer/database-choose/database-choose.component';
import { ModalDirective } from '../directives/modal.directive';
import {JwtInterceptor} from './authorization/jwt-interceptor';
import { AccountSettingsComponent } from './settings/account-settings/account-settings.component';
import { MinLengthValidatorDirective } from '../directives/min-length-validator.directive';
import { DatabaseAccessModifierComponent } from './database-edit/database-access-modifier/database-access-modifier.component';
import { DatabaseAccessModifierService } from './database-edit/database-access-modifier/database-access-modifier.service';
import { DatabaseMergerComponent } from './database-edit/database-merger/database-merger.component';
import {CookieService} from 'ngx-cookie-service';
import { HintComponent } from './common/hint/hint.component';
import { ChessEnginesViewerComponent } from './chessboard-viewer/chess-engines-viewer/chess-engines-viewer.component';
import { CurrentIpDisplayerComponent } from './login-screen/current-ip-displayer/current-ip-displayer.component';

const appRoutes: Routes = [
  {path: ROUTING_PATHS.CHESSBOARD_VIEWER, component: ChessboardViewerComponent},
  {path: ROUTING_PATHS.GAMES_LIST, component: GamesListPanelComponent},
  {path: ROUTING_PATHS.SETTINGS, component: SettingsComponent},
  {path: ROUTING_PATHS.DB_EDIT, component: DatabaseEditComponent},
  {path: ROUTING_PATHS.DB_FILTER, component: FilterDbComponent},
  {path: ROUTING_PATHS.DEFAULT, component: GamesListPanelComponent},
];


@NgModule({
  declarations: [
    AppComponent,
    ChessboardViewerComponent,
    LeftSidePanelComponent,
    GamesListPanelComponent,
    PgnRowDisplayComponent,
    LoginScreenComponent,
    ButtonDirective,
    InputDirective,
    InputErrorListComponent,
    ErrorCodePipe,
    GameHeadersViewerComponent,
    SettingsComponent,
    LoadingWhenDirective,
    InfoMessageComponent,
    LoadingComponent,
    PaginationChangerComponent,
    TrimPipe,
    RoundButtonComponent,
    ModalMessageComponent,
    FileUploaderComponent,
    DatabaseEditComponent,
    ActionDropdownComponent,
    PlainRowDisplayComponent,
    GameViewerComponent,
    GameEntityViewerComponent,
    AboutScreenComponent,
    MoveActionPanelComponent,
    GameActionPanelComponent,
    ButtonWithActionConfirmationComponent,
    DatabaseAdderComponent,
    FilterDbComponent,
    CancelConfirmInputComponent,
    MaxLengthValidatorDirective,
    HeaderAndPositionFilterFormComponent,
    HeadersFilterComponent,
    PositionsFilterComponent,
    BooleanButtonsComponent,
    GameDisplaySettingsComponent,
    MaxValueValidatorDirective,
    MinValueValidatorDirective,
    GenericSettingsComponent,
    HeaderAutocompleteInputComponent,
    StatusBarComponent,
    GameViewerBottomPanelComponent,
    GeneralRadioButtonsComponent,
    RadioButtonComponent,
    TreeViewerComponent,
    PromotePieceComponent,
    DatabaseChooseComponent,
    ModalDirective,
    AccountSettingsComponent,
    MinLengthValidatorDirective,
    DatabaseAccessModifierComponent,
    DatabaseMergerComponent,
    HintComponent,
    ChessEnginesViewerComponent,
    CurrentIpDisplayerComponent
  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule.forRoot(
      appRoutes,
      {enableTracing: false}
    )
  ],
  providers: [
    LibloaderService,
    ChessboardService,
    AuthenticationService,
    ServerService,
    PaginationService,
    GameStateReactorService,
    ModalMessageService,
    DatabaseAccessModifierService,
    CookieService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [AppComponent],
  entryComponents: [LoadingComponent, ModalMessageComponent, PromotePieceComponent]
})
export class AppModule {
}
