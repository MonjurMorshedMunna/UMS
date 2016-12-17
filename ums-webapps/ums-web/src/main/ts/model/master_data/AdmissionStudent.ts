/**
 * Created by My Pc on 17-Dec-16.
 */

module ums{
  export interface AdmissionStudent{
    semesterId:number;
    semesterName:string;
    receiptId:string;
    pin:string;
    hscBoard:string;
    hscRoll:string;
    hscRegNo:string;
    hscYear:number;
    hscGroup:string;
    sscBoard:string;
    sscRoll:string;
    sscRegNo:string;
    sscYear:number;
    sscGroup:string;
    gender:string;
    dateOfBirth:string;
    studentName:string;
    fatherName:string;
    motherName:string;
    sscGpa:any;
    hscGpa:any;
    quota:string;
    admissionRoll:string;
    meritSlNo:number;
    studentId:string;
    allocatedProgramId:number;
    programShortName:string;
    programLongName:string;
  }
}