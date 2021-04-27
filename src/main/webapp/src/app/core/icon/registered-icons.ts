import {IconSvgModel} from './icon-svg.model';

function svgIcon(name: string, fileName?: string): IconSvgModel {
  fileName = fileName ? fileName : name;
  return new IconSvgModel(name, fileName);
}

export const REGISTERED_ICONS: IconSvgModel[] = [

  svgIcon('info')

];


