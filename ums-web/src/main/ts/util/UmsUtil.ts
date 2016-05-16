module ums {
  export class UmsUtil {
    public static getNumberWithSuffix(n: number): string {
      var suffix: string = "";
      switch (n) {
        case 1:
          suffix = "st";
          break;
        case 2:
          suffix = "nd";
          break;
        case 3:
          suffix = "rd";
          break;
        default:
          suffix = "th";
          break;
      }
      return n + "" + suffix;
    }

    public static isEmpty(obj: any) {
      return Object.keys(obj).length === 0;
    }

    public static isEmptyString(str: string) {
      return (typeof str === 'undefined' || str === '' || str == null);
    }
  }
}