///<reference path="ProgramSelectorModelImpl.ts"/>
module ums {
  export class SemesterSyllabusMapModel extends ProgramSelectorModelImpl {
    semesterId: string;
    syllabuses:any;
    constructor(appConstants:any, httpClient:HttpClient) {
      super(appConstants, httpClient);

      this.semesterId = '';

    }
  }
}