import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
/**
 * Service that will load chessboardjs and append it into DOM
 */
export class LibloaderService {

  constructor() { }

  public loadChessBoardJs() {
    if (document.getElementById("board") == null) {
      console.log("DBG: no element with id board while loading chessboardjs");
      return;
    }
    if (document.getElementById("chessboardjsscript") != null) {
      // let element = document.getElementById('chessboardjsscript');
      // element.parentNode.removeChild(element);
      return;
    }
    let url = "src/chessboardjs/js/chessboard-0.3.0.js";
    console.log('preparing to load...');
    let node = document.createElement('script');
    node.id = "chessboardjsscript";
    node.src = url;
    node.type = 'text/javascript';
    node.async = true;
    node.charset = 'utf-8';
    document.getElementsByTagName('head')[0].appendChild(node);
  }

  public getLoadPromise() {
    return new Promise((resolve) => {
      console.log('resolving promise...');
      this.loadChessBoardJs();
    });
  }

  public deloadChessBoardJs() {
    let element = document.getElementById('chessboardjsscript');
    if (element != null) {
      element.parentNode.removeChild(element);
    }
  }

  public reloadChessBoardJs() {
    this.deloadChessBoardJs();
    this.loadChessBoardJs();
  }

}
