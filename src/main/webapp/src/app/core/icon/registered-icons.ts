import {IconSvgModel} from './icon-svg.model';

function register(name: string, fileName?: string): IconSvgModel {
  fileName = fileName ? fileName : name;
  return new IconSvgModel(name, fileName);
}

export const REGISTERED_ICONS: IconSvgModel[] = [

  register('info'),
  register('login'),
  register('logout'),
  register('user_management'),
  register('visibility'),
  register('visibility_off')

];


