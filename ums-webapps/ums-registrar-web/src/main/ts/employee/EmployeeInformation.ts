module ums {
    interface IEmployeeInformation extends ng.IScope{
        submitPersonalForm: Function;
        submitAcademicForm: Function;
        submitPublicationForm: Function;
        submitTrainingForm: Function;
        submitAwardForm: Function;
        submitExperienceForm: Function;
        submitAdditionalForm: Function;
        submitServiceForm: Function;
        getEmployeeInformation: Function;
    }
    export interface IDepartment{
        id: string;
        shortName: string;
        longName: string;
        type: string;
    }

    class EmployeeInformation {
        public static $inject = ['registrarConstants', '$scope', '$q', 'notify',
            'countryService', 'divisionService', 'districtService', 'thanaService',
            'employeeInformationService','areaOfInterestService', 'userService',
            'cRUDDetectionService', 'employmentTypeService', 'departmentService', 'designationService', 'employeeService'];

        private entry: {
            personal: IPersonalInformationModel,
            academic: IAcademicInformationModel[],
            publication: IPublicationInformationModel[],
            training: ITrainingInformationModel[],
            award: IAwardInformationModel[],
            experience: IExperienceInformationModel[],
            additional: IAdditionalInformationModel,
            serviceInfo: IServiceInformationModel[]
        };
        private personal: boolean = true;
        private academic: boolean = false;
        private publication: boolean = false;
        private training: boolean = false;
        private award: boolean = false;
        private experience: boolean = false;
        private additional: boolean = false;
        private service: boolean = false;
        private showPersonalInputDiv: boolean = false;
        private showAcademicInputDiv: boolean = false;
        private showPublicationInputDiv: boolean = false;
        private showTrainingInputDiv: boolean = false;
        private showAwardInputDiv: boolean = false;
        private showExperienceInputDiv: boolean = false;
        private showAdditionalInputDiv: boolean = false;
        private showServiceInputDiv: boolean = false;
        private required: boolean = false;
        private disablePresentAddressDropdown: boolean = false;
        private disablePermanentAddressDropdown: boolean = false;
        private showServiceEditButton: boolean = true;
        private data: any;
        private itemPerPage: number = 2;
        private currentPage: number = 1;
        private totalRecord: number = 0;
        private genders: IGender[] = [];
        private maritalStatus: ICommon[] = [];
        private religions: ICommon[] = [];
        private nationalities: ICommon[] = [];
        private degreeNames: ICommon[] = [];
        private bloodGroups: ICommon[] = [];
        private publicationTypes: ICommon[] = [];
        private relations: ICommon[] = [];
        private countries: ICommon[] = [];
        private divisions: ICommon[] = [];
        private presentAddressDistricts: ICommon[] = [];
        private permanentAddressDistricts: ICommon[] = [];
        private allDistricts: ICommon[] = [];
        private presentAddressThanas: ICommon[] = [];
        private permanentAddressThanas: ICommon[] = [];
        private allThanas: ICommon[] = [];
        private arrayOfAreaOfInterest: ICommon[] = [];
        private paginatedPublication: IPublicationInformationModel[];
        private previousPersonalInformation: IPersonalInformationModel;
        private previousAcademicInformation: IAcademicInformationModel[];
        private previousPublicationInformation: IPublicationInformationModel[];
        private previousTrainingInformation: ITrainingInformationModel[];
        private previousAwardInformation: IAwardInformationModel[];
        private previousExperienceInformation: IExperienceInformationModel[];
        private previousServiceInformation: IServiceInformationModel[];
        private academicDeletedObjects: IAcademicInformationModel[];
        private publicationDeletedObjects: IPublicationInformationModel[];
        private trainingDeletedObjects: ITrainingInformationModel[];
        private awardDeletedObjects: IAwardInformationModel[];
        private experienceDeletedObjects: IExperienceInformationModel[];
        private serviceDeletedObjects: IServiceInformationModel[];
        private serviceDetailDeletedObjects: IServiceDetailsModel[];
        private userId: string = "";

        private filterData: string;
        private searchBy: string;
        private changedUserName: string;
        private showSearchByUserId: boolean;
        private showSearchByUserName: boolean;
        private showSearchByDepartment: boolean;
        private showFilteredEmployeeList: boolean;
        private showEmployeeInformation: boolean;
        private showSelectPanel: boolean;
        private showTableData: boolean;
        private changedDepartment: IDepartment;
        private allUser: Array<Employee>;
        private designations: ICommon[] = [];
        private employmentTypes: ICommon[] = [];
        private serviceIntervals: ICommon[] = [];
        private serviceRegularIntervals: ICommon[] = [];
        private serviceContractIntervals: ICommon[] = [];
        private departments: IDepartment[] = [];

        constructor(private registrarConstants: any,
                    private $scope: IEmployeeInformation,
                    private $q: ng.IQService,
                    private notify: Notify,
                    private countryService: CountryService,
                    private divisionService: DivisionService,
                    private districtService: DistrictService,
                    private thanaService: ThanaService,
                    private employeeInformationService: EmployeeInformationService,
                    private areaOfInterestService: AreaOfInterestService,
                    private userService: UserService,
                    private cRUDDetectionService: CRUDDetectionService,
                    private employmentTypeService: EmploymentTypeService,
                    private departmentService: DepartmentService,
                    private designationService: DesignationService,
                    private employeeService: EmployeeService) {

            $scope.submitPersonalForm = this.submitPersonalForm.bind(this);
            $scope.submitAcademicForm = this.submitAcademicForm.bind(this);
            $scope.submitPublicationForm = this.submitPublicationForm.bind(this);
            $scope.submitTrainingForm = this.submitTrainingForm.bind(this);
            $scope.submitAwardForm = this.submitAwardForm.bind(this);
            $scope.submitExperienceForm = this.submitExperienceForm.bind(this);
            $scope.submitAdditionalForm = this.submitAdditionalForm.bind(this);
            $scope.submitServiceForm = this.submitServiceForm.bind(this);
            $scope.getEmployeeInformation = this.getEmployeeInformation.bind(this);

            this.entry = {
                personal: <IPersonalInformationModel> {},
                academic: Array<IAcademicInformationModel>(),
                publication: Array<IPublicationInformationModel>(),
                training: Array<ITrainingInformationModel>(),
                award: Array<IAwardInformationModel>(),
                experience: Array<IExperienceInformationModel>(),
                additional: <IAdditionalInformationModel> {},
                serviceInfo: Array<IServiceInformationModel>()
            };

            this.data = {
                supOptions: "1",
                borderColor: "",
                changedUserId: "",
                changedUserName: "",
                searchBy: "",
                changedDepartment: null,
                filterData: ""
            };
            this.genders = this.registrarConstants.genderTypes;
            this.publicationTypes = this.registrarConstants.publicationTypes;
            this.degreeNames = this.registrarConstants.degreeTypes;
            this.bloodGroups = this.registrarConstants.bloodGroupTypes;
            this.maritalStatus = this.registrarConstants.maritalStatuses;
            this.religions = this.registrarConstants.religionTypes;
            this.relations = this.registrarConstants.relationTypes;
            this.nationalities = this.registrarConstants.nationalityTypes;
            this.serviceIntervals = registrarConstants.servicePeriods;
            this.initialization();
        }

        private initialization() {
            this.employeeService.getAll().then((users: any) => {
                this.allUser = users;
                this.showEmployeeInformation = false;
                this.showSelectPanel = true;
                this.showFilteredEmployeeList = true;
                this.countryService.getCountryList().then((countries: any) => {
                    this.countries = countries.entries;
                    this.divisionService.getDivisionList().then((divisions: any) => {
                        this.divisions = divisions.entries;
                        this.districtService.getDistrictList().then((districts: any) => {
                            this.presentAddressDistricts = districts.entries;
                            this.permanentAddressDistricts = districts.entries;
                            this.allDistricts = districts.entries;
                            this.thanaService.getThanaList().then((thanas: any) => {
                                this.presentAddressThanas = thanas.entries;
                                this.permanentAddressThanas = thanas.entries;
                                this.allThanas = thanas.entries;
                                this.areaOfInterestService.getAll().then((aois: any) => {
                                    this.arrayOfAreaOfInterest = aois;
                                    this.departmentService.getAll().then((departments: any) => {
                                        this.departments = departments;
                                        this.designationService.getAll().then((designations: any) => {
                                            this.designations = designations;
                                            this.employmentTypeService.getAll().then((employmentTypes: any) => {
                                                this.employmentTypes = employmentTypes;
                                                this.getServiceIntervals();
                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        }

        private getServiceIntervals(): void {
            this.serviceRegularIntervals.push(this.registrarConstants.servicePeriods[0]);
            this.serviceRegularIntervals.push(this.registrarConstants.servicePeriods[1]);
            this.serviceRegularIntervals.push(this.registrarConstants.servicePeriods[2]);
            this.serviceContractIntervals.push(this.registrarConstants.servicePeriods[3]);
        }

        private view(user: any): void{
            this.userId = user.id;
            this.showFilteredEmployeeList = false;
            this.showEmployeeInformation = true;
            this.showTab('personal');
            Utils.expandRightDiv();
        }

        private getSearchFields(): void{
            if(this.data.searchBy == "1"){
                this.showSearchByUserName = false;
                this.showSearchByDepartment = false;
                this.showFilteredEmployeeList = false;
                this.showSearchByUserId = true;
                this.showEmployeeInformation = true;
            }
            else if(this.data.searchBy == "2"){
                this.showSearchByUserId = false;
                this.showSearchByDepartment = false;
                this.showEmployeeInformation = false;
                this.showSearchByUserName = true;
                this.showFilteredEmployeeList = true;
            }
            else if(this.data.searchBy == "3"){
                this.showSearchByUserId = false;
                this.showSearchByUserName = false;
                this.showEmployeeInformation = false;
                this.showFilteredEmployeeList = true;
                this.showSearchByDepartment = true;
            }
        }

        private getEmployeeInformation(): void{
            if(this.data.searchBy == "1"){
                if(this.findUser() == true) {
                    this.userId = this.data.changedUserId;
                    this.showFilteredEmployeeList = false;
                    this.showEmployeeInformation = true;
                    if (this.userId == "" || this.userId == null) {
                        this.notify.error("Empty or Null Id");
                    }
                    else {
                        Utils.expandRightDiv();
                        this.showTab("personal");
                    }
                }
                else{
                    this.notify.error("No User Found");
                }
            }
            else if (this.data.searchBy == "2"){
                this.showEmployeeInformation = false;
                this.showFilteredEmployeeList = true;
                if(this.data.changedUserName != null || this.data.changedUserName != ""){
                    this.showTableData = true;
                    Utils.expandRightDiv();
                }
                else{
                    this.showTableData = false;
                }
                this.data.filterData = this.data.changedUserName;
            }
            else if (this.data.searchBy == "3"){
                Utils.expandRightDiv();
                this.showEmployeeInformation = false;
                this.showFilteredEmployeeList = true;
            }
        }

        private findUser(): boolean{
            let length = this.allUser.length;
            for(let i = 0; i < length; i++){
                if(this.allUser[i].id == this.data.changedUserId){
                    return true;
                }
            }
            return false;
        }

        private showTab(formName: string){
            if (formName === "personal") {
                this.getPersonalInformation();
                this.academic = false;
                this.publication = false;
                this.training = false;
                this.award = false;
                this.experience = false;
                this.additional = false;
                this.service = false;
                this.personal = true;
            }
            else if (formName === "academic") {
                this.getAcademicInformation();
                this.personal = false;
                this.publication = false;
                this.training = false;
                this.award = false;
                this.experience = false;
                this.additional = false;
                this.service = false;
                this.academic = true;
            }
            else if (formName === "publication") {
                this.getPublicationInformation();
                this.personal = false;
                this.academic = false;
                this.training = false;
                this.award = false;
                this.experience = false;
                this.additional = false;
                this.service = false;
                this.publication = true;
            }
            else if (formName === "training") {
                this.getTrainingInformation();
                this.personal = false;
                this.academic = false;
                this.publication = false;
                this.award = false;
                this.experience = false;
                this.additional = false;
                this.service = false;
                this.training = true;
            }
            else if (formName === "award") {
                this.getAwardInformation();
                this.personal = false;
                this.academic = false;
                this.publication = false;
                this.training = false;
                this.experience = false;
                this.additional = false;
                this.service = false;
                this.award = true;
            }
            else if (formName === "experience") {
                this.getExperienceInformation();
                this.personal = false;
                this.academic = false;
                this.publication = false;
                this.training = false;
                this.award = false;
                this.additional = false;
                this.service = false;
                this.experience = true;
            }
            else if(formName === 'additional'){
                this.getAdditionalInformation();
                this.personal = false;
                this.academic = false;
                this.publication = false;
                this.training = false;
                this.award = false;
                this.experience = false;
                this.service = false;
                this.additional = true;
            }
            else if(formName === 'service'){
                this.getServiceInformation();
                this.personal = false;
                this.academic = false;
                this.publication = false;
                this.training = false;
                this.award = false;
                this.experience = false;
                this.additional = false;
                this.service = true;
            }
        }

        private testData(): void {
            this.entry.personal.firstName = "Kawsur";
            this.entry.personal.lastName = "Mir Md.";
            this.entry.personal.fatherName = "Mir Abdul Aziz";
            this.entry.personal.motherName = "Mst Hosne Ara";
            this.entry.personal.gender = this.genders[1];
            this.entry.personal.dateOfBirth = "20/10/1995";
            this.entry.personal.nationality = this.nationalities[1];
            this.entry.personal.religion = this.religions[1];
            this.entry.personal.maritalStatus = this.maritalStatus[1];
            this.entry.personal.spouseName = "";
            this.entry.personal.nidNo = "19952641478954758";
            this.entry.personal.spouseNidNo = "";
            this.entry.personal.bloodGroup = this.bloodGroups[1];
            this.entry.personal.website = "https://www.kawsur.com";
            this.entry.personal.organizationalEmail = "kawsur.iums@aust.edu";
            this.entry.personal.personalEmail = "kawsurilu@yahoo.com";
            this.entry.personal.mobile = "+8801672494863";
            this.entry.personal.phone = "none";
            this.entry.personal.emergencyContactName = "None";
            this.entry.personal.emergencyContactRelation = this.relations[0];
            this.entry.personal.emergencyContactPhone = "01898889851";
        }

        private submitPersonalForm(): void {
            this.entry.personal.employeeId = this.userId;
            if (this.cRUDDetectionService.isObjectEmpty(this.previousPersonalInformation)) {
                this.convertToJson('personal', this.entry.personal)
                    .then((json: any) => {
                        this.employeeInformationService.savePersonalInformation(json)
                            .then((message: any) => {
                                this.getPersonalInformation();
                                this.showPersonalInputDiv = false;
                            });
                    });
            }
            else {
                this.convertToJson('personal', this.entry.personal)
                    .then((json: any) => {
                        this.employeeInformationService.updatePersonalInformation(json)
                            .then((message: any) => {
                                this.getPersonalInformation();
                                this.showPersonalInputDiv = false;
                            });
                    });
            }
        }

        private submitAcademicForm(): void {
            this.cRUDDetectionService.ObjectDetectionForCRUDOperation(this.previousAcademicInformation, angular.copy(this.entry.academic), this.academicDeletedObjects)
                .then((academicObjects:any)=>{
                    this.convertToJson('academic', academicObjects)
                        .then((json: any) => {
                            this.employeeInformationService.saveAcademicInformation(json)
                                .then((message: any) => {
                                    this.getAcademicInformation();
                                    this.showAcademicInputDiv = false;
                                });
                        });
                });
        }

        private submitPublicationForm(): void {
            this.cRUDDetectionService.ObjectDetectionForCRUDOperation(this.previousPublicationInformation, angular.copy(this.entry.publication), this.publicationDeletedObjects)
                .then((publicationObjects: any) => {
                    this.convertToJson('publication', publicationObjects)
                        .then((json: any) => {
                            this.employeeInformationService.savePublicationInformation(json)
                                .then((message: any) => {
                                    this.getPublicationInformation();
                                    this.getPublicationInformationWithPagination();
                                    this.showPublicationInputDiv = false;
                                });
                        });
                });
        }

        private submitTrainingForm(): void {
            this.cRUDDetectionService.ObjectDetectionForCRUDOperation(this.previousTrainingInformation, angular.copy(this.entry.training), this.trainingDeletedObjects)
                .then((trainingObjects: any)=>{
                    this.convertToJson('training', trainingObjects)
                        .then((json: any) => {
                            this.employeeInformationService.saveTrainingInformation(json)
                                .then((message: any) => {
                                    this.getTrainingInformation();
                                    this.showTrainingInputDiv = false;
                                });
                        });
                });
        }

        private submitAwardForm(): void {
            this.cRUDDetectionService.ObjectDetectionForCRUDOperation(this.previousAwardInformation, angular.copy(this.entry.award), this.awardDeletedObjects)
                .then((awardObjects: any)=>{
                    this.convertToJson('award', awardObjects)
                        .then((json: any) => {
                            this.employeeInformationService.saveAwardInformation(json)
                                .then((message: any) => {
                                    this.getAwardInformation();
                                    this.showAwardInputDiv = false;
                                });
                        });
                });

        }

        private submitExperienceForm(): void {
            this.cRUDDetectionService.ObjectDetectionForCRUDOperation(this.previousExperienceInformation, angular.copy(this.entry.experience), this.experienceDeletedObjects)
                .then((experienceObjects: any)=>{
                    this.convertToJson('experience', experienceObjects)
                        .then((json: any) => {
                            this.employeeInformationService.saveExperienceInformation(json)
                                .then((message: any) => {
                                    this.getExperienceInformation();
                                    this.showExperienceInputDiv = false;
                                });
                        });
                });
        }

        private submitAdditionalForm(): void {
            this.entry.additional.employeeId = this.userId;
            this.convertToJson('additional', this.entry.additional)
                .then((json: any) => {
                    this.employeeInformationService.saveAdditionalInformation(json)
                        .then((message: any) => {
                            this.getAdditionalInformation();
                            this.showAdditionalInputDiv = false;
                        });
                });
        }

        private submitServiceForm(): void {
            let countEmptyResignDate = 0;
            for (let i = 0; i < this.entry.serviceInfo.length; i++) {
                if (this.entry.serviceInfo[i].resignDate == "" || this.entry.serviceInfo[i].resignDate == null) {
                    countEmptyResignDate++;
                }
            }
            if (countEmptyResignDate > 1) {
                this.notify.error("You can empty only one resign date");
            }
            else {
                this.cRUDDetectionService.ObjectDetectionForServiceObjects(this.previousServiceInformation, angular.copy(this.entry.serviceInfo), this.serviceDeletedObjects, this.serviceDetailDeletedObjects)
                    .then((serviceObjects) => {
                        this.convertToJson('service', serviceObjects).then((json: any) => {
                            this.employeeInformationService.saveServiceInformation(json).then((message: any) => {
                                this.getServiceInformation();
                                this.showServiceInputDiv = false;
                            });
                        });
                    });
            }
        }

        private getPersonalInformation() {
            this.entry.personal = <IPersonalInformationModel>{};
            this.employeeInformationService.getPersonalInformation(this.userId).then((personalInformation: any) => {
                if (personalInformation.length > 0) {
                    this.entry.personal = personalInformation[0];
                }
            }).then(() => {
                this.previousPersonalInformation = angular.copy(this.entry.personal);
            });
        }

        private getAcademicInformation() {
            this.entry.academic = [];
            this.academicDeletedObjects = [];
            this.employeeInformationService.getAcademicInformation(this.userId).then((academicInformation: any) => {
                this.entry.academic = academicInformation;
            }).then(() => {
                this.previousAcademicInformation = angular.copy(this.entry.academic);
            });
        }

        private getPublicationInformation() {
            this.entry.publication = [];
            this.publicationDeletedObjects = [];
            this.employeeInformationService.getPublicationInformation(this.userId).then((publicationInformation: any) => {
                this.totalRecord = publicationInformation.length;
                this.entry.publication = publicationInformation;
            }).then(() => {
                this.previousPublicationInformation = angular.copy(this.entry.publication);
            });
        }

        private getPublicationInformationWithPagination(){
            this.paginatedPublication = [];
            this.employeeInformationService.getPublicationInformationViewWithPagination(this.userId, this.currentPage, this.itemPerPage).then((publicationInformationWithPagination: any) => {
                this.paginatedPublication = publicationInformationWithPagination;
            });
        }

        private getTrainingInformation() {
            this.entry.training = [];
            this.trainingDeletedObjects = [];
            this.employeeInformationService.getTrainingInformation(this.userId).then((trainingInformation: any) => {
                this.entry.training = trainingInformation;
            }).then(()=>{
                this.previousTrainingInformation = angular.copy(this.entry.training);
            });
        }

        private getAwardInformation() {
            this.entry.award = [];
            this.awardDeletedObjects = [];
            this.employeeInformationService.getAwardInformation(this.userId).then((awardInformation: any) => {
                this.entry.award = awardInformation;
            }).then(() =>{
                this.previousAwardInformation = angular.copy(this.entry.award);
            });
        }

        private getExperienceInformation() {
            this.entry.experience = [];
            this.experienceDeletedObjects = [];
            this.employeeInformationService.getExperienceInformation(this.userId).then((experienceInformation: any) => {
                this.entry.experience = experienceInformation;
            }).then(() => {
                this.previousExperienceInformation = angular.copy(this.entry.experience);
            });
        }
        private getAdditionalInformation() {
            this.entry.additional = <IAdditionalInformationModel>{};
            this.employeeInformationService.getAdditionalInformation(this.userId).then((additional: any) => {
                this.entry.additional = additional[0];
            });
        }

        private getServiceInformation(): void {
            this.entry.serviceInfo = [];
            this.serviceDeletedObjects = [];
            this.serviceDetailDeletedObjects = [];
            this.employeeInformationService.getServiceInformation(this.userId).then((services: any) => {
                this.entry.serviceInfo = services;
            }).then(()=>{
               this.previousServiceInformation = angular.copy(this.entry.serviceInfo);
            });
        }

        private changePresentAddressDistrict() {
            this.presentAddressDistricts = [];
            let districtLength = this.allDistricts.length;
            let index = 0;
            for (let i = 0; i < districtLength; i++) {
                if (this.entry.personal.preAddressDivision.id === this.allDistricts[i].foreign_id) {
                    this.presentAddressDistricts[index++] = this.allDistricts[i];
                }
            }
        }

        private changePermanentAddressDistrict() {
            this.permanentAddressDistricts = [];
            let districtLength = this.allDistricts.length;
            let index = 0;
            for (let i = 0; i < districtLength; i++) {
                if (this.entry.personal.perAddressDivision.id === this.allDistricts[i].foreign_id) {
                    this.permanentAddressDistricts[index++] = this.allDistricts[i];
                }
            }
        }

        private changePresentAddressThana() {
            this.presentAddressThanas = [];
            let thanaLength = this.allThanas.length;
            let index = 0;
            for (let i = 0; i < thanaLength; i++) {
                if (this.entry.personal.preAddressDistrict.id === this.allThanas[i].foreign_id) {
                    this.presentAddressThanas[index++] = this.allThanas[i];
                }
            }
        }

        private changePermanentAddressThana() {
            this.permanentAddressThanas = [];
            let thanaLength = this.allThanas.length;
            let index = 0;
            for (let i = 0; i < thanaLength; i++) {
                if (this.entry.personal.perAddressDistrict.id === this.allThanas[i].foreign_id) {
                    this.permanentAddressThanas[index++] = this.allThanas[i];
                }
            }
        }

        private sameAsPresentAddress() {
            this.entry.personal.perAddressLine1 = this.entry.personal.preAddressLine1;
            this.entry.personal.perAddressLine2 = this.entry.personal.preAddressLine2;
            this.entry.personal.perAddressCountry = this.entry.personal.preAddressCountry;
            this.entry.personal.perAddressDivision = this.entry.personal.preAddressDivision;
            this.entry.personal.perAddressDistrict = this.entry.personal.preAddressDistrict;
            this.entry.personal.perAddressThana = this.entry.personal.preAddressThana;
            this.entry.personal.perAddressPostCode = this.entry.personal.preAddressPostCode;
            this.changePermanentAddressFields();
        }

        private changePresentAddressFields() {
            if (this.entry.personal.preAddressCountry.name === "Bangladesh") {
                this.required = true;
                this.disablePresentAddressDropdown = false;
                this.changePresentAddressDistrict();
                this.changePresentAddressThana();
            }
            else {
                this.required = false;
                this.disablePresentAddressDropdown = true;
                this.entry.personal.preAddressDivision = null;
                this.entry.personal.preAddressDistrict = null;
                this.entry.personal.preAddressThana = null;
                this.entry.personal.preAddressPostCode = null;
            }
        }

        private changePermanentAddressFields() {
            if (this.entry.personal.perAddressCountry.name === "Bangladesh") {
                this.disablePermanentAddressDropdown = false;
                this.changePermanentAddressDistrict();
                this.changePermanentAddressThana();
            }
            else {
                this.disablePermanentAddressDropdown = true;
                this.entry.personal.perAddressDivision = null;
                this.entry.personal.perAddressDistrict = null;
                this.entry.personal.perAddressThana = null;
                this.entry.personal.perAddressPostCode = null;
            }
        }

        private fillEmergencyContactAddress() {
            if (this.data.supOptions === "1") {
                this.entry.personal.emergencyContactAddress = "";
            }
            else if (this.data.supOptions === "2") {
                this.entry.personal.emergencyContactAddress =
                    this.entry.personal.preAddressLine1 == null ? "" : this.entry.personal.preAddressLine1
                    + " " + this.entry.personal.preAddressLine2 == null ? "" : this.entry.personal.preAddressLine2
                    + " " + this.entry.personal.preAddressThana.name == null ? "" : this.entry.personal.preAddressThana.name
                    + " " + this.entry.personal.preAddressDistrict.name == null ? "" : this.entry.personal.preAddressDistrict.name
                    + " - " + this.entry.personal.preAddressPostCode == null ? "" : this.entry.personal.preAddressPostCode;
            }
            else if (this.data.supOptions === "3") {
                this.entry.personal.emergencyContactAddress =
                    this.entry.personal.perAddressLine1 == null ? "" : this.entry.personal.perAddressLine1
                    + " " + this.entry.personal.perAddressLine2 == null ? "" : this.entry.personal.perAddressLine2
                    + " " + this.entry.personal.perAddressThana.name == null ? "" : this.entry.personal.perAddressThana.name
                    + " " + this.entry.personal.perAddressDistrict.name == null ? "" : this.entry.personal.perAddressDistrict.name
                    + " - " + this.entry.personal.perAddressPostCode == null ? "" : this.entry.personal.perAddressPostCode;
            }
        }

        private pageChanged(pageNumber: number){
            this.currentPage = pageNumber;
            this.getPublicationInformationWithPagination();
        }

        private changeItemPerPage(){
            if(this.data.customItemPerPage == "" || this.data.customItemPerPage == null) {}
            else{
                this.data.itemPerPage = this.data.customItemPerPage;
                this.getPublicationInformationWithPagination();
            }
        }

        private addNewServiceRow(parameter: string, index?: number): void {
            if(parameter == "serviceInfo") {
                if(this.entry.serviceInfo.length == 0){
                    this.addNewRow("service");
                }
                else {
                    if(this.entry.serviceInfo[this.entry.serviceInfo.length - 1].resignDate == ""){
                        this.notify.error("Please fill up the resign date first");
                    }
                    else {
                        this.addNewRow("service");
                    }
                }
            }
            else if(parameter == "serviceDetails") {
                if(this.entry.serviceInfo[index].intervalDetails.length == 0) {
                    this.addNewServiceDetailsRow(index);
                }
                else{
                    if(this.entry.serviceInfo[index].intervalDetails[this.entry.serviceInfo[index].intervalDetails.length - 1].endDate == ""){
                        this.notify.error("Please fill up the end date first");
                    }
                    else {
                        this.addNewServiceDetailsRow(index);
                    }
                }
            }
        }

        private addNewServiceDetailsRow(index: number) {
            let serviceDetailsEntry: IServiceDetailsModel;
            serviceDetailsEntry = {
                id: "",
                interval: null,
                startDate: "",
                endDate: "",
                serviceId: null,
                dbAction: "Create"
            };
            this.entry.serviceInfo[index].intervalDetails.push(serviceDetailsEntry);
        }

        private addNewRow(divName: string) {
            if (divName === 'academic') {
                let academicEntry: IAcademicInformationModel;
                academicEntry = {
                    id: "",
                    employeeId: this.userId,
                    degree: null,
                    institution: "",
                    passingYear: null,
                    dbAction: "Create"
                };
                this.entry.academic.push(academicEntry);
            }
            else if (divName === 'publication') {
                let publicationEntry: IPublicationInformationModel;
                publicationEntry = {
                    id: "",
                    employeeId: this.userId,
                    publicationTitle: "",
                    publicationType: null,
                    publicationInterestGenre: "",
                    publicationWebLink: "",
                    publisherName: "",
                    dateOfPublication: "",
                    publicationISSN: "",
                    publicationIssue: "",
                    publicationVolume: "",
                    publicationJournalName: "",
                    publicationCountry: null,
                    status: "",
                    publicationPages: "",
                    appliedOn: "",
                    actionTakenOn: "",
                    rowNumber: null,
                    dbAction: "Create"
                };
                this.entry.publication.push(publicationEntry);
            }
            else if (divName === 'training') {
                let trainingEntry: ITrainingInformationModel;
                trainingEntry = {
                    id: "",
                    employeeId: this.userId,
                    trainingName: "",
                    trainingInstitution: "",
                    trainingFrom: "",
                    trainingTo: "",
                    dbAction: "Create"
                };
                this.entry.training.push(trainingEntry);
            }
            else if (divName === 'award') {
                let awardEntry: IAwardInformationModel;
                awardEntry = {
                    id: "",
                    employeeId: this.userId,
                    awardName: "",
                    awardInstitute: "",
                    awardedYear: null,
                    awardShortDescription: "",
                    dbAction: "Create"
                };
                this.entry.award.push(awardEntry);
            }
            else if (divName === 'experience') {
                let experienceEntry: IExperienceInformationModel;
                experienceEntry = {
                    id: "",
                    employeeId: this.userId,
                    experienceInstitution: "",
                    experienceDesignation: "",
                    experienceFrom: "",
                    experienceTo: "",
                    dbAction: "Create"
                };
                this.entry.experience.push(experienceEntry);
            }
            else if(divName = 'service'){
                let serviceEntry: IServiceInformationModel;
                serviceEntry = {
                    id: "",
                    employeeId: this.userId,
                    department: null,
                    designation: null,
                    employmentType: null,
                    joiningDate: "",
                    resignDate: "",
                    dbAction: "Create",
                    intervalDetails: Array<IServiceDetailsModel>()
                };
                this.entry.serviceInfo.push(serviceEntry);
            }
        }

        private deleteRow(divName: string, index: number, parentIndex?: number) {
            if (divName === 'academic') {
                if(this.entry.academic[index].id != ""){
                    this.entry.academic[index].dbAction = "Delete";
                    this.academicDeletedObjects.push(this.entry.academic[index]);
                }
                this.entry.academic.splice(index, 1);
            }
            else if (divName === 'publication') {
                if(this.entry.publication[index].id != ""){
                    this.entry.publication[index].dbAction = "Delete";
                    this.publicationDeletedObjects.push(this.entry.publication[index]);
                }
                this.entry.publication.splice(index, 1);
            }
            else if (divName === 'training') {
                if(this.entry.training[index].id != ""){
                    this.entry.training[index].dbAction = "Delete";
                    this.trainingDeletedObjects.push(this.entry.training[index]);
                }
                this.entry.training.splice(index, 1);
            }
            else if (divName === 'award') {
                if(this.entry.award[index].id != ""){
                    this.entry.award[index].dbAction = "Delete";
                    this.awardDeletedObjects.push(this.entry.award[index]);
                }
                this.entry.award.splice(index, 1);
            }
            else if (divName === 'experience') {
                if(this.entry.experience[index].id != ""){
                    this.entry.experience[index].dbAction = "Delete";
                    this.experienceDeletedObjects.push(this.entry.experience[index]);
                }
                this.entry.experience.splice(index, 1);
            }
            else if(divName == "serviceInfo") {
                if(this.entry.serviceInfo[index].id != "") {
                    this.entry.serviceInfo[index].dbAction = "Delete";
                    this.serviceDeletedObjects.push(this.entry.serviceInfo[index]);
                }
                this.entry.serviceInfo.splice(index, 1);
            }
            else if(divName == "serviceDetails"){
                if(this.entry.serviceInfo[parentIndex].intervalDetails[index].id != "") {
                    this.entry.serviceInfo[parentIndex].intervalDetails[index].dbAction = "Delete";
                    this.serviceDetailDeletedObjects.push(this.entry.serviceInfo[parentIndex].intervalDetails[index]);
                }
                this.entry.serviceInfo[parentIndex].intervalDetails.splice(index, 1);
            }
        }

        private convertToJson(convertThis: string, obj: any): ng.IPromise<any> {
            let defer = this.$q.defer();
            let JsonObject = {};
            let JsonArray = [];
            let item: any = {};
            if (convertThis === "personal") {
                item['personal'] = obj;
            }
            else if (convertThis === "academic") {
                item['academic'] = obj;
            }
            else if (convertThis === "publication"){
                item['publication'] = obj;
            }
            else if (convertThis === "training") {
                item['training'] = obj;
            }
            else if (convertThis === "award") {
                item['award'] = obj;
            }
            else if (convertThis === "experience") {
                item['experience'] = obj;
            }
            else if (convertThis === "additional") {
                item['additional'] = obj;
            }
            else if (convertThis === "service") {
                item['service'] = obj;
            }
            JsonArray.push(item);
            JsonObject['entries'] = JsonArray;
            defer.resolve(JsonObject);
            return defer.promise;
        }
    }
    UMS.controller("EmployeeInformation", EmployeeInformation);
}