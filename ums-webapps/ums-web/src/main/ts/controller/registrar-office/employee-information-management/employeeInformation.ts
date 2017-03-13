module ums{
  interface IEmployeeInformation extends ng.IScope{
    personal: boolean;
    academic: boolean;
    publication: boolean;
    training: boolean;
    award: boolean;





    //testData
    experience: boolean;
    firstName: string;
    lastName: string;
    fatherName: string;
    motherName: string;
    gender: string;
    birthday: string;
    maritalStatus: string;
    spouseName: string;
    nationalIdNo: number;
    email: string;
    phone: string;
    presentAddress: string;
    permanentAddress: string;

    sscSelected: string;
    sscInstitution: string;
    sscGPA: number;
    sscPassingYear: string;
    hscSelected: string;
    hscInstitution: string;
    hscGPA: number;
    hscPassingYear: string;
    bachelorSelected: string;
    bachelorInstitution: string;
    bachelorCGPA: number;
    bachelorPassingYear: string;
    mastersSelected:string;
    mastersInstitution: string;
    mastersCGPA: number;
    mastersPassingYear: string;
    phdSelected: string;
    phdInstitution: string;
    phdCGPA: number;
    phdPassingYear: string;

    publicationTitle:string;
    publicationType: string;
    publicationYear: string;
    publicationPlace: string;
    webReference: string;

    trainingName: string;
    trainingInstitution: string;
    trainingDuration: string;
    trainingAccreditedYear: string;

    awardName: string;
    purpose: string;
    yearOfRecognition: string;
    description: string;

    experienceInstitution: string;
    experienceDesignation: string;
    experienceFromMonth: string;
    experienceFromYear: string;
    experienceToMonth: string;
    experienceToYear: string;


    showInputFirstNameDiv: boolean;
    showLabelFirstNameDiv: boolean;
    showInputLastNameDiv: boolean;
    showLabelLastNameDiv: boolean;
    showInputFatherNameDiv: boolean;
    showLabelFatherNameDiv: boolean;
    showInputMotherNameDiv: boolean;
    showLabelMotherNameDiv: boolean;
    showInputGenderDiv: boolean;
    showLabelGenderDiv: boolean;
    showInputBirthdayDiv: boolean;
    showLabelBirthdayDiv: boolean;
    showInputMaritalStatusDiv: boolean;
    showLabelMaritalStatusDiv: boolean;
    showInputSpouseNameDiv: boolean;
    showLabelSpouseNameDiv: boolean;
    showInputNationalIdNoDiv: boolean;
    showLabelNationalIdNoDiv: boolean;
    showInputEmailDiv: boolean;
    showLabelEmailDiv: boolean;
    showInputPhoneDiv: boolean;
    showLabelPhoneDiv: boolean;
    showInputPresentAddressDiv: boolean;
    showLabelPresentAddressDiv: boolean;
    showInputPermanentAddressDiv: boolean;
    showLabelPermanentAddressDiv: boolean;
    showInputSSCSelectedDiv: boolean;
    showLabelSSCSelectedDiv: boolean;
    showInputSSCInstitutionDiv: boolean;
    showLabelSSCInstitutionDiv: boolean;
    showInputSSCGpaDiv: boolean;
    showLabelSSCGpaDiv: boolean;
    showInputSSCPassingYearDiv: boolean;
    showLabelSSCPassingYearDiv: boolean;
    showInputHSCSelectedDiv: boolean;
    showLabelHSCSelectedDiv: boolean;
    showInputHSCInstitutionDiv: boolean;
    showLabelHSCInstitutionDiv: boolean;
    showInputHSCGpaDiv: boolean;
    showLabelHSCGpaDiv: boolean;
    showInputHSCPassingYearDiv: boolean;
    showLabelHSCPassingYearDiv: boolean;
    showInputBachelorSelectedDiv: boolean;
    showLabelBachelorSelectedDiv: boolean;
    showInputBachelorInstitutionDiv: boolean;
    showLabelBachelorInstitutionDiv: boolean;
    showInputBachelorCGpaDiv: boolean;
    showLabelBachelorCGpaDiv: boolean;
    showInputBachelorPassingYearDiv: boolean;
    showLabelBachelorPassingYearDiv: boolean;
    showInputMastersSelectedDiv: boolean;
    showLabelMastersSelectedDiv: boolean;
    showInputMastersInstitutionDiv: boolean;
    showLabelMastersInstitutionDiv: boolean;
    showInputMastersCGpaDiv: boolean;
    showLabelMastersCGpaDiv: boolean;
    showInputMastersPassingYearDiv: boolean;
    showLabelMastersPassingYearDiv: boolean;
    showInputPhdSelectedDiv: boolean;
    showLabelPhdSelectedDiv: boolean;
    showInputPhdInstitutionDiv: boolean;
    showLabelPhdInstitutionDiv: boolean;
    showInputPhdCGpaDiv: boolean;
    showLabelPhdCGpaDiv: boolean;
    showInputPhdPassingYearDiv: boolean;
    showLabelPhdPassingYearDiv: boolean;
    showInputPublicationTitleDiv: boolean;
    showLabelPublicationTitleDiv: boolean;
    showInputPublicationTypeDiv: boolean;
    showLabelPublicationTypeDiv: boolean;
    showInputPublicationYearDiv: boolean;
    showLabelPublicationYearDiv: boolean;
    showInputPublicationPlaceDiv: boolean;
    showLabelPublicationPlaceDiv: boolean;
    showInputWebReferenceDiv: boolean;
    showLabelWebReferenceDiv: boolean;
    showInputTrainingNameDiv: boolean;
    showLabelTrainingNameDiv: boolean;
    showInputTrainingInstitutionDiv: boolean;
    showLabelTrainingInstitutionDiv: boolean;
    showInputTrainingDurationDiv: boolean;
    showLabelTrainingDurationDiv: boolean;
    showInputTrainingAccreditedYearDiv: boolean;
    showLabelTrainingAccreditedYearDiv: boolean;
    showInputAwardNameDiv: boolean;
    showLabelAwardNameDiv: boolean;
    showInputPurposeDiv: boolean;
    showLabelPurposeDiv: boolean;
    showInputYearOfRecognitionDiv: boolean;
    showLabelYearOfRecognitionDiv: boolean;
    showInputDescriptionDiv: boolean;
    showLabelDescriptionDiv: boolean;
    showInputExperienceInstitutionDiv: boolean;
    showLabelExperienceInstitutionDiv: boolean;
    showInputExperienceDesignationDiv: boolean;
    showLabelExperienceDesignationDiv: boolean;
    showInputExperienceFromMonthDiv: boolean;
    showLabelExperienceFromMonthDiv: boolean;
    showInputExperienceFromYearDiv: boolean;
    showLabelExperienceFromYearDiv: boolean;
    showInputExperienceToMonthDiv: boolean;
    showLabelExperienceToMonthDiv: boolean;
    showInputExperienceToYearDiv: boolean;
    showLabelExperienceToYearDiv: boolean;







    changeNav: Function;
    submit: Function;
    cancel: Function;

    //test Data Function
    testData: Function;
  }

  class employeeInformation {

    public static $inject = ['appConstants', '$scope', '$q', 'notify', '$window', '$sce'];

    constructor(private appConstants: any,
                private $scope: IEmployeeInformation,
                private $q: ng.IQService,
                private notify: Notify,
                private $window: ng.IWindowService,
                private $sce: ng.ISCEService) {

      $scope.personal = true;
      $scope.showInputFirstNameDiv = true;
      $scope.showLabelFirstNameDiv = false;
      $scope.showInputLastNameDiv = true;
      $scope.showLabelLastNameDiv = false;
      $scope.showInputFatherNameDiv = true;
      $scope.showLabelFatherNameDiv = false;
      $scope.showInputMotherNameDiv = true;
      $scope.showLabelMotherNameDiv = false;
      $scope.showInputGenderDiv = true;
      $scope.showLabelGenderDiv = false;
      $scope.showInputBirthdayDiv = true;
      $scope.showLabelBirthdayDiv = false;
      $scope.showInputMaritalStatusDiv = true;
      $scope.showLabelMaritalStatusDiv = false;
      $scope.showInputSpouseNameDiv = true;
      $scope.showLabelSpouseNameDiv = false;
      $scope.showInputNationalIdNoDiv = true;
      $scope.showLabelNationalIdNoDiv = false;
      $scope.showInputEmailDiv = true;
      $scope.showLabelEmailDiv = false;
      $scope.showInputPhoneDiv = true;
      $scope.showLabelPhoneDiv = false;
      $scope.showInputPresentAddressDiv = true;
      $scope.showLabelPresentAddressDiv = false;
      $scope.showInputPermanentAddressDiv = true;
      $scope.showLabelPermanentAddressDiv = false;
      $scope.showInputSSCSelectedDiv = true;
      $scope.showLabelSSCSelectedDiv = false;
      $scope.showInputSSCInstitutionDiv = true;
      $scope.showLabelSSCInstitutionDiv = false;
      $scope.showInputSSCGpaDiv = true;
      $scope.showLabelSSCGpaDiv = false;
      $scope.showInputSSCPassingYearDiv = true;
      $scope. showLabelSSCPassingYearDiv = false;
      $scope.showInputHSCSelectedDiv = true;
      $scope.showLabelHSCSelectedDiv = false;
      $scope.showInputHSCInstitutionDiv = true;
      $scope.showLabelHSCInstitutionDiv = false;
      $scope.showInputHSCGpaDiv = true;
      $scope.showLabelHSCGpaDiv = false;
      $scope.showInputHSCPassingYearDiv = true;
      $scope. showLabelHSCPassingYearDiv = false;
      $scope.showInputBachelorSelectedDiv = true;
      $scope.showLabelBachelorSelectedDiv = false;
      $scope.showInputBachelorInstitutionDiv = true;
      $scope.showLabelBachelorInstitutionDiv = false;
      $scope.showInputBachelorCGpaDiv = true;
      $scope.showLabelBachelorCGpaDiv = false;
      $scope.showInputBachelorPassingYearDiv = true;
      $scope.showLabelBachelorPassingYearDiv = false;
      $scope.showInputMastersSelectedDiv = true;
      $scope.showLabelMastersSelectedDiv = false;
      $scope.showInputMastersInstitutionDiv = true;
      $scope.showLabelMastersInstitutionDiv = false;
      $scope.showInputMastersCGpaDiv = true;
      $scope.showLabelMastersCGpaDiv = false;
      $scope.showInputMastersPassingYearDiv = true;
      $scope.showLabelMastersPassingYearDiv = false;
      $scope.showInputPhdSelectedDiv = true;
      $scope.showLabelPhdSelectedDiv = false;
      $scope.showInputPhdInstitutionDiv = true;
      $scope.showLabelPhdInstitutionDiv = false;
      $scope.showInputPhdCGpaDiv = true;
      $scope.showLabelPhdCGpaDiv = false;
      $scope.showInputPhdPassingYearDiv = true;
      $scope.showLabelPhdPassingYearDiv = false;
      $scope.showInputPublicationTitleDiv = true;
      $scope.showLabelPublicationTitleDiv = false;
      $scope.showInputPublicationTypeDiv = true;
      $scope.showLabelPublicationTypeDiv = false;
      $scope.showInputPublicationYearDiv = true;
      $scope.showLabelPublicationYearDiv = false;
      $scope.showInputPublicationPlaceDiv = true;
      $scope.showLabelPublicationPlaceDiv = false;
      $scope.showInputWebReferenceDiv = true;
      $scope.showLabelWebReferenceDiv = false;
      $scope.showInputTrainingNameDiv = true;
      $scope.showLabelTrainingNameDiv = false;
      $scope.showInputTrainingInstitutionDiv = true;
      $scope.showLabelTrainingInstitutionDiv = false;
      $scope.showInputTrainingDurationDiv = true;
      $scope.showLabelTrainingDurationDiv = false;
      $scope.showInputTrainingAccreditedYearDiv = true;
      $scope.showLabelTrainingAccreditedYearDiv = false;
      $scope.showInputAwardNameDiv = true;
      $scope.showLabelAwardNameDiv = false;
      $scope.showInputPurposeDiv = true;
      $scope.showLabelPurposeDiv = false;
      $scope.showInputYearOfRecognitionDiv = true;
      $scope.showLabelYearOfRecognitionDiv = false;
      $scope.showInputDescriptionDiv = true;
      $scope.showLabelDescriptionDiv = false;
      $scope.showInputExperienceInstitutionDiv = true;
      $scope.showLabelExperienceInstitutionDiv = false;
      $scope.showInputExperienceDesignationDiv = true;
      $scope.showLabelExperienceDesignationDiv = false;
      $scope.showInputExperienceFromMonthDiv = true;
      $scope.showLabelExperienceFromMonthDiv = false;
      $scope.showInputExperienceFromYearDiv = true;
      $scope.showLabelExperienceFromYearDiv = false;
      $scope.showInputExperienceToMonthDiv = true;
      $scope.showLabelExperienceToMonthDiv = false;
      $scope.showInputExperienceToYearDiv = true;
      $scope.showLabelExperienceToYearDiv = false;


      $scope.changeNav = this.changeNav.bind(this);
      $scope.testData = this.testData.bind(this);
      $scope.submit = this.submit.bind(this);
      $scope.cancel = this.cancel.bind(this);
    }


    private changeNav(navTitle: number){

      this.$scope.personal = false;
      this.$scope.academic = false;
      this.$scope.publication = false;
      this.$scope.training = false;
      this.$scope.award = false;
      this.$scope.experience = false;

      if(navTitle == null){

      }
      else if(navTitle == 1){
        this.$scope.personal = true;
      }
      else if(navTitle == 2){
        this.$scope.academic = true;
      }
      else if(navTitle == 3){
        this.$scope.publication = true;
      }
      else if(navTitle == 4){
        this.$scope.training = true;
      }
      else if(navTitle == 5){
        this.$scope.award = true;
      }
      else if(navTitle == 6){
        this.$scope.experience = true;
      }
    }




    // Used For Test Data
    private testData(){
      this.$scope.firstName = "kawsur";
      this.$scope.lastName = "Mir Md.";
      this.$scope.fatherName = "Mir Abdul Aziz";
      this.$scope.motherName = "Mst Hosne Ara";
      this.$scope.gender = "Male";
      this.$scope.birthday = "20/10/1995";
      this.$scope.maritalStatus = "Single";
      this.$scope.spouseName = "";
      this.$scope.nationalIdNo = 19952641478954758;
      this.$scope.email = "kawsur.iums@aust.edu";
      this.$scope.phone = "+8801672494863";
      this.$scope.presentAddress = "34/1 K R Road Posta Lalbagh Dhaka-1211, Bangladesh";
      this.$scope.permanentAddress = "Don't Know";

      this.$scope.sscSelected = "SSC";
      this.$scope.sscInstitution = "Birsrestha Munshi Abdur Rouf Public College";
      this.$scope.sscGPA = 5.00;
      this.$scope.sscPassingYear = "2010";
      this.$scope.hscSelected = "HSC";
      this.$scope.hscInstitution = "Birsrestha Munshi Abdur Rouf Public College";
      this.$scope.hscGPA = 5.00;
      this.$scope.hscPassingYear = "2012";
      this.$scope.bachelorSelected = "Bachelor";
      this.$scope.bachelorInstitution = "American International University-Bangladesh";
      this.$scope.bachelorCGPA = 3.79;
      this.$scope.bachelorPassingYear = "2016";
      this.$scope.mastersSelected = "Masters";
      this.$scope.mastersInstitution = "American International University-Bangladesh";
      this.$scope.mastersCGPA = 4.00;
      this.$scope.mastersPassingYear = "2018";
      this.$scope.phdSelected = "Phd";
      this.$scope.phdInstitution = "Massachusetts Institute Of Technology";
      this.$scope.phdCGPA = 4.00;
      this.$scope.phdPassingYear = "2022";

      this.$scope.publicationTitle = "IoT Species: Identifying And Communicating Between Homogeneous Devices";
      this.$scope.publicationType = "Journal";
      this.$scope.publicationYear = "2017";
      this.$scope.publicationPlace = "IEEE";
      this.$scope.webReference = "Unavailable";

      this.$scope.trainingName = "Oracle Java SE Course";
      this.$scope.trainingInstitution = "Oracle University";
      this.$scope.trainingDuration = "6";
      this.$scope.trainingAccreditedYear = "2017";

      this.$scope.awardName = "Best Student Award";
      this.$scope.purpose = "";
      this.$scope.yearOfRecognition = "2016";
      this.$scope.description = "Thanks For The Award";

      this.$scope.experienceInstitution = "American International University-Bangladesh";
      this.$scope.experienceDesignation = "Teacher's Assistant";
      this.$scope.experienceFromMonth = "09";
      this.$scope.experienceFromYear = "2016";
      this.$scope.experienceToMonth = "10";
      this.$scope.experienceToYear = "2016";
    }


    private submit(){
      this.$scope.showInputFirstNameDiv = false;
      this.$scope.showLabelFirstNameDiv = true;
      this.$scope.showInputLastNameDiv = false;
      this.$scope.showLabelLastNameDiv = true;
      this.$scope.showInputFatherNameDiv = false;
      this.$scope.showLabelFatherNameDiv = true;
      this.$scope.showInputMotherNameDiv = false;
      this.$scope.showLabelMotherNameDiv = true;
      this.$scope.showInputGenderDiv = false;
      this.$scope.showLabelGenderDiv = true;
      this.$scope.showInputBirthdayDiv = false;
      this.$scope.showLabelBirthdayDiv = true;
      this.$scope.showInputMaritalStatusDiv = false;
      this.$scope.showLabelMaritalStatusDiv = true;
      this.$scope.showInputSpouseNameDiv = false;
      this.$scope.showLabelSpouseNameDiv = true;
      this.$scope.showInputNationalIdNoDiv = false;
      this.$scope.showLabelNationalIdNoDiv = true;
      this.$scope.showInputEmailDiv = false;
      this.$scope.showLabelEmailDiv = true;
      this.$scope.showInputPhoneDiv = false;
      this.$scope.showLabelPhoneDiv = true;
      this.$scope.showInputPresentAddressDiv = false;
      this.$scope.showLabelPresentAddressDiv = true;
      this.$scope.showInputPermanentAddressDiv = false;
      this.$scope.showLabelPermanentAddressDiv = true;
      this.$scope.showInputSSCSelectedDiv = false;
      this.$scope.showLabelSSCSelectedDiv = true;
      this.$scope.showInputSSCInstitutionDiv = false;
      this.$scope.showLabelSSCInstitutionDiv = true;
      this.$scope.showInputSSCGpaDiv = false;
      this.$scope.showLabelSSCGpaDiv = true;
      this.$scope.showInputSSCPassingYearDiv = false;
      this.$scope.showLabelSSCPassingYearDiv = true;
      this.$scope.showInputHSCSelectedDiv = false;
      this.$scope.showLabelHSCSelectedDiv = true;
      this.$scope.showInputHSCInstitutionDiv = false;
      this.$scope.showLabelHSCInstitutionDiv = true;
      this.$scope.showInputHSCGpaDiv = false;
      this.$scope.showLabelHSCGpaDiv = true;
      this.$scope.showInputHSCPassingYearDiv = false;
      this.$scope.showLabelHSCPassingYearDiv = true;
      this.$scope.showInputBachelorSelectedDiv = false;
      this.$scope.showLabelBachelorSelectedDiv = true;
      this.$scope.showInputBachelorInstitutionDiv = false;
      this.$scope.showLabelBachelorInstitutionDiv = true;
      this.$scope.showInputBachelorCGpaDiv = false;
      this.$scope.showLabelBachelorCGpaDiv = true;
      this.$scope.showInputBachelorPassingYearDiv = false;
      this.$scope.showLabelBachelorPassingYearDiv = true;
      this.$scope.showInputMastersSelectedDiv = false;
      this.$scope.showLabelMastersSelectedDiv = true;
      this.$scope.showInputMastersInstitutionDiv = false;
      this.$scope.showLabelMastersInstitutionDiv = true;
      this.$scope.showInputMastersCGpaDiv = false;
      this.$scope.showLabelMastersCGpaDiv = true;
      this.$scope.showInputMastersPassingYearDiv = false;
      this.$scope.showLabelMastersPassingYearDiv = true;
      this.$scope.showInputPhdSelectedDiv = false;
      this.$scope.showLabelPhdSelectedDiv = true;
      this.$scope.showInputPhdInstitutionDiv = false;
      this.$scope.showLabelPhdInstitutionDiv = true;
      this.$scope.showInputPhdCGpaDiv = false;
      this.$scope.showLabelPhdCGpaDiv = true;
      this.$scope.showInputPhdPassingYearDiv = false;
      this.$scope.showLabelPhdPassingYearDiv = true;
      this.$scope.showInputPublicationTitleDiv = false;
      this.$scope.showLabelPublicationTitleDiv = true;
      this.$scope.showInputPublicationTypeDiv = false;
      this.$scope.showLabelPublicationTypeDiv = true;
      this.$scope.showInputPublicationYearDiv = false;
      this.$scope.showLabelPublicationYearDiv = true;
      this.$scope.showInputPublicationPlaceDiv = false;
      this.$scope.showLabelPublicationPlaceDiv = true;
      this.$scope.showInputWebReferenceDiv = false;
      this.$scope.showLabelWebReferenceDiv = true;
      this.$scope.showInputTrainingNameDiv = false;
      this.$scope.showLabelTrainingNameDiv = true;
      this.$scope.showInputTrainingInstitutionDiv = false;
      this.$scope.showLabelTrainingInstitutionDiv = true;
      this.$scope.showInputTrainingDurationDiv = false;
      this.$scope.showLabelTrainingDurationDiv = true;
      this.$scope.showInputTrainingAccreditedYearDiv = false;
      this.$scope.showLabelTrainingAccreditedYearDiv = true;
      this.$scope.showInputAwardNameDiv = false;
      this.$scope.showLabelAwardNameDiv = true;
      this.$scope.showInputPurposeDiv = false;
      this.$scope.showLabelPurposeDiv = true;
      this.$scope.showInputYearOfRecognitionDiv = false;
      this.$scope.showLabelYearOfRecognitionDiv = true;
      this.$scope.showInputDescriptionDiv = false;
      this.$scope.showLabelDescriptionDiv = true;
      this.$scope.showInputExperienceInstitutionDiv = false;
      this.$scope.showLabelExperienceInstitutionDiv = true;
      this.$scope.showInputExperienceDesignationDiv = false;
      this.$scope.showLabelExperienceDesignationDiv = true;
      this.$scope.showInputExperienceFromMonthDiv = false;
      this.$scope.showLabelExperienceFromMonthDiv = true;
      this.$scope.showInputExperienceFromYearDiv = false;
      this.$scope.showLabelExperienceFromYearDiv = true;
      this.$scope.showInputExperienceToMonthDiv = false;
      this.$scope.showLabelExperienceToMonthDiv = true;
      this.$scope.showInputExperienceToYearDiv = false;
      this.$scope.showLabelExperienceToYearDiv = true;


      this.notify.success("Data Saved");

    }

    private cancel(){
      this.$scope.firstName = "";
      this.$scope.lastName = "";
      this.$scope.fatherName = "";
      this.$scope.motherName = "";
      this.$scope.gender = "";
      this.$scope.birthday = "";
      this.$scope.maritalStatus = "";
      this.$scope.spouseName = "";
      this.$scope.nationalIdNo = null;
      this.$scope.email = "";
      this.$scope.phone = "";
      this.$scope.presentAddress = "";
      this.$scope.permanentAddress = "";

      this.$scope.sscSelected = "";
      this.$scope.sscInstitution = "";
      this.$scope.sscGPA = null;
      this.$scope.sscPassingYear = "";
      this.$scope.hscSelected = "";
      this.$scope.hscInstitution = "";
      this.$scope.hscGPA = null;
      this.$scope.hscPassingYear = "";
      this.$scope.bachelorSelected = "";
      this.$scope.bachelorInstitution = "";
      this.$scope.bachelorCGPA = null;
      this.$scope.bachelorPassingYear = "";
      this.$scope.mastersSelected = "";
      this.$scope.mastersInstitution = "";
      this.$scope.mastersCGPA = null;
      this.$scope.mastersPassingYear = "";
      this.$scope.phdSelected = "";
      this.$scope.phdInstitution = "";
      this.$scope.phdCGPA = null;
      this.$scope.phdPassingYear = "";

      this.$scope.publicationTitle = "";
      this.$scope.publicationType = "";
      this.$scope.publicationYear = "";
      this.$scope.publicationPlace = "";
      this.$scope.webReference = "";

      this.$scope.trainingName = "";
      this.$scope.trainingInstitution = "";
      this.$scope.trainingDuration = "";
      this.$scope.trainingAccreditedYear = "";

      this.$scope.awardName = "";
      this.$scope.purpose = "";
      this.$scope.yearOfRecognition = "";
      this.$scope.description = "";

      this.$scope.experienceInstitution = "";
      this.$scope.experienceDesignation = "";
      this.$scope.experienceFromMonth = "";
      this.$scope.experienceFromYear = "";
      this.$scope.experienceToMonth = "";
      this.$scope.experienceToYear = "";



      this.$scope.showInputFirstNameDiv = true;
      this.$scope.showLabelFirstNameDiv = false;
      this.$scope.showInputLastNameDiv = true;
      this.$scope.showLabelLastNameDiv = false;
      this.$scope.showInputFatherNameDiv = true;
      this.$scope.showLabelFatherNameDiv = false;
      this.$scope.showInputMotherNameDiv = true;
      this.$scope.showLabelMotherNameDiv = false;
      this.$scope.showInputGenderDiv = true;
      this.$scope.showLabelGenderDiv = false;
      this.$scope.showInputBirthdayDiv = true;
      this.$scope.showLabelBirthdayDiv = false;
      this.$scope.showInputMaritalStatusDiv = true;
      this.$scope.showLabelMaritalStatusDiv = false;
      this.$scope.showInputSpouseNameDiv = true;
      this.$scope.showLabelSpouseNameDiv = false;
      this.$scope.showInputNationalIdNoDiv = true;
      this.$scope.showLabelNationalIdNoDiv = false;
      this.$scope.showInputEmailDiv = true;
      this.$scope.showLabelEmailDiv = false;
      this.$scope.showInputPhoneDiv = true;
      this.$scope.showLabelPhoneDiv = false;
      this.$scope.showInputPresentAddressDiv = true;
      this.$scope.showLabelPresentAddressDiv = false;
      this.$scope.showInputPermanentAddressDiv = true;
      this.$scope.showLabelPermanentAddressDiv = false;
      this.$scope.showInputSSCSelectedDiv = true;
      this.$scope.showLabelSSCSelectedDiv = false;
      this.$scope.showInputSSCInstitutionDiv = true;
      this.$scope.showLabelSSCInstitutionDiv = false;
      this.$scope.showInputSSCGpaDiv = true;
      this.$scope.showLabelSSCGpaDiv = false;
      this.$scope.showInputSSCPassingYearDiv = true;
      this.$scope.showLabelSSCPassingYearDiv = false;
      this.$scope.showInputHSCSelectedDiv = true;
      this.$scope.showLabelHSCSelectedDiv = false;
      this.$scope.showInputHSCInstitutionDiv = true;
      this.$scope.showLabelHSCInstitutionDiv = false;
      this.$scope.showInputHSCGpaDiv = true;
      this.$scope.showLabelHSCGpaDiv = false;
      this.$scope.showInputHSCPassingYearDiv = true;
      this.$scope.showLabelHSCPassingYearDiv = false;
      this.$scope.showInputBachelorSelectedDiv = true;
      this.$scope.showLabelBachelorSelectedDiv = false;
      this.$scope.showInputBachelorInstitutionDiv = true;
      this.$scope.showLabelBachelorInstitutionDiv = false;
      this.$scope.showInputBachelorCGpaDiv = true;
      this.$scope.showLabelBachelorCGpaDiv = false;
      this.$scope.showInputBachelorPassingYearDiv = true;
      this.$scope.showLabelBachelorPassingYearDiv = false;
      this.$scope.showInputMastersSelectedDiv = true;
      this.$scope.showLabelMastersSelectedDiv = false;
      this.$scope.showInputMastersInstitutionDiv = true;
      this.$scope.showLabelMastersInstitutionDiv = false;
      this.$scope.showInputMastersCGpaDiv = true;
      this.$scope.showLabelMastersCGpaDiv = false;
      this.$scope.showInputMastersPassingYearDiv = true;
      this.$scope.showLabelMastersPassingYearDiv = false;
      this.$scope.showInputPhdSelectedDiv = true;
      this.$scope.showLabelPhdSelectedDiv = false;
      this.$scope.showInputPhdInstitutionDiv = true;
      this.$scope.showLabelPhdInstitutionDiv = false;
      this.$scope.showInputPhdCGpaDiv = true;
      this.$scope.showLabelPhdCGpaDiv = false;
      this.$scope.showInputPhdPassingYearDiv = true;
      this.$scope.showLabelPhdPassingYearDiv = false;
      this.$scope.showInputPublicationTitleDiv = true;
      this.$scope.showLabelPublicationTitleDiv = false;
      this.$scope.showInputPublicationTypeDiv = true;
      this.$scope.showLabelPublicationTypeDiv = false;
      this.$scope.showInputPublicationYearDiv = true;
      this.$scope.showLabelPublicationYearDiv = false;
      this.$scope.showInputPublicationPlaceDiv = true;
      this.$scope.showLabelPublicationPlaceDiv = false;
      this.$scope.showInputWebReferenceDiv = true;
      this.$scope.showLabelWebReferenceDiv = false;
      this.$scope.showInputTrainingNameDiv = true;
      this.$scope.showLabelTrainingNameDiv = false;
      this.$scope.showInputTrainingInstitutionDiv = true;
      this.$scope.showLabelTrainingInstitutionDiv = false;
      this.$scope.showInputTrainingDurationDiv = true;
      this.$scope.showLabelTrainingDurationDiv = false;
      this.$scope.showInputTrainingAccreditedYearDiv = true;
      this.$scope.showLabelTrainingAccreditedYearDiv = false;
      this.$scope.showInputAwardNameDiv = true;
      this.$scope.showLabelAwardNameDiv = false;
      this.$scope.showInputPurposeDiv = true;
      this.$scope.showLabelPurposeDiv = false;
      this.$scope.showInputYearOfRecognitionDiv = true;
      this.$scope.showLabelYearOfRecognitionDiv = false;
      this.$scope.showInputDescriptionDiv = true;
      this.$scope.showLabelDescriptionDiv = false;
      this.$scope.showInputExperienceInstitutionDiv = true;
      this.$scope.showLabelExperienceInstitutionDiv = false;
      this.$scope.showInputExperienceDesignationDiv = true;
      this.$scope.showLabelExperienceDesignationDiv = false;
      this.$scope.showInputExperienceFromMonthDiv = true;
      this.$scope.showLabelExperienceFromMonthDiv = false;
      this.$scope.showInputExperienceFromYearDiv = true;
      this.$scope.showLabelExperienceFromYearDiv = false;
      this.$scope.showInputExperienceToMonthDiv = true;
      this.$scope.showLabelExperienceToMonthDiv = false;
      this.$scope.showInputExperienceToYearDiv = true;
      this.$scope.showLabelExperienceToYearDiv = false;
    }
  }

  UMS.controller("employeeInformation",employeeInformation);
}