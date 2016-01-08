module ums {
  export class Constants {
    static  Default():any {
      return {
        Empty:"",
        initDept: [{id:'',name:'Select Dept./School'}],
        initProgram: [{id: '', longName: 'Select a Program'}],
        initSemester: [{id: '', name: 'Select a Semester'}],
        initSyllabus: [{id: '', semester_name: 'Select a ',program_name:'Syllabus'}],
        courseType: [
          {id: '', name: 'Select Course Type'},
          {id: '1', name: 'Theory'},
          {id: '2', name: 'Sessional'},
          {id: '3', name: 'Thesis/Project'}
        ],
        courseCategory: [
          {id: '', name: 'Select Course Category'},
          {id: '1', name: 'Mandatory'},
          {id: '2', name: 'Optional'}
        ],
        academicYear: [
          {id: '', name: 'Select Year'},
          {id: '1', name: '1st Year'},
          {id: '2', name: '2nd Year'},
          {id: '3', name: '3rd Year'},
          {id: '4', name: '4th Year'}
        ],
        academicSemester: [
          {id: '', name: 'Select Semester'},
          {id: '1', name: '1st Semester'},
          {id: '2', name: '2nd Semester'}
        ],
        semesterType: [
          {id: '', name: 'Select Semester'},
          {id: '01', name: 'Spring'},
          {id: '02', name: 'Fall'}
        ],
        gender: [
          {id: '', name: 'Select Gender'},
          {id: 'M', name: 'Male'},
          {id: 'F', name: 'Female'}
        ],
        programType: [
          {id: '', name: 'Select Program Type'},
          {id: '11', name: 'Undergraduate'},
          {id: '22', name: 'Postgraduate'}
        ],
        deptShort: [
          {id: '', name: 'Select Dept./School'},
          {id: '01', name: 'ARC'},
          {id: '02', name: 'BBA'},
          {id: '03', name: 'CE'},
          {id: '04', name: 'CSE'},
          {id: '05', name: 'EEE'},
          {id: '06', name: 'TE'},
          {id: '07', name: 'MPE'}
        ],
        deptLong: {
          '01': 'Department of Architecture',
          '02': 'School of Business',
          '03': 'Department of Civil Engineering',
          '04': 'Department of Computer Science and Engineering',
          '05': 'Department of Electrical and Electronic Engineering',
          '06': 'Department of Textile Engineering',
          '07': 'Department of Mechanical & Production Engineering'
        },
        officeShort: {
          '80': 'RO',
          '81': 'CO',
          '82': 'TO',
          '83': 'EO',
          '84': 'AoSW',
          '85': 'PO'
        },
        officeLong: {
          '80': 'Office of the Registrar',
          '81': 'Office of the Controller of Examinations',
          '82': 'Office of the Treasurer',
          '83': 'Engineering Office',
          '84': "Office of the Advisor of Student''s Welfare",
          '85': 'Proctor Office'
        },
        status: {
          '0': 'Inactive',
          '1': 'Active'
        },
        bloodGroup: [
          {id: '', name: 'Select Blood Group'},
          {id: 'O+', name: 'O+'},
          {id: 'O–', name: 'O-'},
          {id: 'A+', name: 'A+'},
          {id: 'A–	', name: 'A-'},
          {id: 'B+', name: 'B+'},
          {id: 'B–', name: 'B-'},
          {id: 'AB+', name: 'AB+'},
          {id: 'AB–', name: 'AB-'}
        ],
        ugDept: [
          {id: '', name: 'Select Dept./School'},
          {id: '01', name: 'ARC'},
          {id: '02', name: 'BBA'},
          {id: '03', name: 'CE'},
          {id: '04', name: 'CSE'},
          {id: '05', name: 'EEE'},
          {id: '06', name: 'TE'},
          {id: '07', name: 'MPE'}
        ],
        pgDept: [
          {id: '', name: 'Select Dept./School'},
          {id: '01', name: 'ARC'},
          {id: '02', name: 'BBA'},
          {id: '05', name: 'EEE'}
        ],
        ugPrograms: [
          {
            deptId: '01',
            programs: [
              {
                id: "110100",
                shortName: "BSC in ARC",
                longName: "Bachelor in Arch. Engineering"
              }
            ]
          },
          {
            deptId: '02',
            programs: [
              {
                id: "110200",
                shortName: "BBA",
                longName: "Bachelor in Business Administration"
              }
            ]
          },
          {
            deptId: "03",
            programs: [
              {
                id: "110300",
                shortName: "BSC in CE",
                longName: "Bachelor in Civil Engineering"
              }
            ]
          },
          {
            deptId: "04",
            programs: [
              {
                id: "110400",
                shortName: "BSC in CSE",
                longName: "Bachelor in Computer Science and Engineering"
              }
            ]
          },
          {
            deptId: "05",
            programs: [
              {
                id: "110500",
                shortName: "BSC in EEE",
                longName: "Bachelor in Electrical and Electronic Engineering"
              }
            ]
          },
          {
            deptId: "06",
            programs: [
              {
                id: "110600",
                shortName: "BSC in TE",
                longName: "Bachelor in Textile Engineering"
              }
            ]
          },
          {
            deptId: "07",
            programs: [
              {
                id: "110707",
                shortName: "BSC in IPE",
                longName: "Bachelor in Industrial and Production Engineering"
              },
              {
                id: "110708",
                shortName: "BSC in ME",
                longName: "Bachelor in Mechanical Engineering"
              }
            ]
          }
        ],
        mimeTypeJson : 'application/json',
        mimeTypePdf : 'application/pdf',
      };
    }
  }
}