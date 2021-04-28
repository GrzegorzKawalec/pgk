const DIR: string = '../../../assets/icons/';
const SUFFIX_FILE: string = '.svg';

export class IconSvgModel {

  constructor(
    private readonly iconName: string,
    private readonly fileName: string
  ) {
  }

  get name(): string {
    return this.iconName;
  }

  get path(): string {
    return DIR + this.fileName + SUFFIX_FILE;
  }

}
