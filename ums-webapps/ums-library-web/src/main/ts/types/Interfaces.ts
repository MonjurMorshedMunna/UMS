module ums {

  export interface IContributorEntry {
    viewOrder: number;
    role: number;
    id: string,
    name: string;
  }
  export interface IContributorDD {
    id: string;
    text: string;
  }
  export interface INoteEntry {
    viewOrder: number;
    note: string;
  }
  export interface ISubjectEntry {
    viewOrder: number;
    subject: string;
  }

  export interface IImprint {
    publisher: number;
    publisherName: string;
    placeOfPublication: string;
    yearDateOfPublication: string;
    copyRightDate: string;
  }
  export interface IPhysicalDescription {
    pagination: string;
    illustrations: string;
    accompanyingMaterials: string;
    dimensions: string;
    paperQuality: string;
  }

  export interface IRecord {
    mfnNo: string;
    language: number;
    languageName: string;
    materialType: number;
    materialTypeName: string;
    status: number;
    statusName: string;
    bindingType: number;
    bindingTypeName: string;
    acqType: number;
    acqTypeName: string;
    title: string;
    subTitle: string;
    gmd: string;
    seriesTitle: string;
    volumeNo: string;
    volumeTitle: string;
    serialIssueNo: string;
    serialNumber: string;
    serialSpecial: string;
    libraryLacks: string;
    changedTitle: string;
    isbn: string;
    issn: string;
    corpAuthorMain: string;
    corpSubBody: string;
    corpCityCountry: string;
    edition: string;
    translateTitleEdition: string;
    frequency: number;
    callNo: string;
    classNo: string;
    callDate: string;
    authorMark: string;
    contributorList: Array<IContributorEntry>; //need separate channel
    imprint: IImprint;
    physicalDescription: IPhysicalDescription;
    subjectList: Array<ISubjectEntry>;
    noteList: Array<INoteEntry>;
    keywords: string;
    contributorJsonString: string;
    noteJsonString: string;
    subjectJsonString: string;
  }

  export interface IItem {
    mfnNo: string;
    itemId: string;
    copyNumber: number;
    accessionNumber: string;
    accessionDate: string;
    barcode: string;
    price: number;
    internalNote: string;
    supplier: ISupplier;
    status: number;
    statusName: string;
  }
  export interface ISupplier {
    id: string;
    name: string;
    email: string;
    contactPerson: string;
    contactNumber: string;
    address: string,
    note: string;
  }
}