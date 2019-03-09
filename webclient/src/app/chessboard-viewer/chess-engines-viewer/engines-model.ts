
export class EnginesQueryResponse {
  success: boolean;
  info: Engine[];
}

export class Engine {
  name: string;
  path: string;
  description: string;
}

export class StartEngineRequest {
  engine: string;
  fen: string;
  seconds: number
}

export class EngineResult {
  fen: string;
  depth: number;
  scoreCentiPawn: number;
  time: string;
  movesChain: string;
}
