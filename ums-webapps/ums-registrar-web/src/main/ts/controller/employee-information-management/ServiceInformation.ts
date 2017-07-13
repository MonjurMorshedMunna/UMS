module ums{

    interface IEmployeeServiceInformation extends ng.IScope{
        entry: IEntry;
        designations: Array<ICommon>;
        employmentTypes: Array<ICommon>;
        serviceIntervals: Array<ICommon>;

        showInputDiv: boolean;
        showLabelDiv: boolean;

        addNewRow: Function;
        deleteRow: Function;
        submit: Function;
        enableEditMode: Function;
    }

    interface IEntry{
        serviceInfo: Array<IServiceInformationModel>;
    }

    class EmployeeServiceInformation{
        public static $inject = ['registrarConstants', '$scope', '$q', 'notify', '$window', '$sce', 'serviceInformationService', 'employmentTypeService', 'departmentService', 'designationService'];
        constructor(private registrarConstants: any,
                    private $scope: IEmployeeServiceInformation,
                    private $q: ng.IQService,
                    private notify: Notify,
                    private $window: ng.IWindowService,
                    private $sce: ng.ISCEService,
                    private serviceInformationService: EmployeeServiceInformationService,
                    private employmentTypeService: EmploymentTypeService,
                    private departmentService: DepartmentService,
                    private designationService: DesignationService) {

            $scope.showInputDiv = false;
            $scope.showLabelDiv = true;

            $scope.entry = {
                serviceInfo: Array<IServiceInformationModel>()
            };

            $scope.serviceIntervals = this.registrarConstants.servicePeriods;

            $scope.addNewRow = this.addNewRow.bind(this);
            $scope.deleteRow =this.deleteRow.bind(this);
            $scope.submit = this.submit.bind(this);
            $scope.enableEditMode = this.enableEditMode.bind(this);
            this.getEmploymentTypes();
            this.getDesignationTypes();
            this.addDate();
            console.log("I am really changing");
        }

        private getEmploymentTypes(){
            this.employmentTypeService.getAll().then((employmentTypes: any)=>{
               this.$scope.employmentTypes = employmentTypes;
            });
        }

        private getDesignationTypes(){
            this.designationService.getAll().then((designations: any) => {
               this.$scope.designations = designations;
            });
        }

        private submit(): void{
            this.notify.success("I got the submit command");
        }

        private enableEditMode(): void{
            this.$scope.showLabelDiv = false;
            this.$scope.showInputDiv = true;
        }

        private addNewRow(parameter: string, index?: number) {
            if(parameter == "serviceInfo") {
                let serviceEntry: IServiceInformationModel;
                serviceEntry = {
                    employeeId: "",
                    designation: null,
                    employmentType: null,
                    designationId: null,
                    employmentTypeId: null,
                    joiningDate: "",
                    resignDate: "",
                    roomNo: "",
                    extNo: "",
                    academicInitial: "",
                    intervalDetails: Array<IServiceDetailsModel>()
                };
                this.$scope.entry.serviceInfo.push(serviceEntry);
            }
            else if(parameter == "serviceDetails") {
                let serviceDetailsEntry: IServiceDetailsModel;
                serviceDetailsEntry = {
                    interval: null,
                    intervalId: null,
                    startDate: "",
                    endDate: "",
                    expEndDate: ""
                };
                this.$scope.entry.serviceInfo[index].intervalDetails.push(serviceDetailsEntry);
                console.log(this.$scope.entry.serviceInfo);
            }
            this.addDate();
        }

        private deleteRow(parameter: any, parentIndex: number, index: number) {
            console.log(parentIndex + " <--->  " + index);
            if(parameter == "serviceInfo") {
                this.$scope.entry.serviceInfo.splice(index, 1);
            }
            else if(parameter == "serviceDetails"){
                this.$scope.entry.serviceInfo[parentIndex].intervalDetails.splice(index, 1);
            }
        }

        private addDate(): void {
            setTimeout(function () {
                $('.datepicker-default').datepicker();
                $('.datepicker-default').on('change', function () {
                    $('.datepicker').hide();
                });
            }, 100);
        }
    }
    UMS.controller("EmployeeServiceInformation", EmployeeServiceInformation);
}