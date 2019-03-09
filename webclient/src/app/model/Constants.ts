import {Subject} from 'rxjs';

export class Constants {
  public static START_FEN = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR';
  public static SERVER_IP = 'localhost:9095';
  public static serverIpChanged: Subject<void> = new Subject<void>();
}
