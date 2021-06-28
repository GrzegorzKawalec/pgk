import {IconSvgModel} from './icon-svg.model';

function register(name: string, fileName?: string): IconSvgModel {
  fileName = fileName ? fileName : name;
  return new IconSvgModel(name, fileName);
}

export const REGISTERED_ICONS: IconSvgModel[] = [

  register('add'),
  register('clear'),
  register('delete'),
  register('edit'),
  register('info'),
  register('legal_act_add'),
  register('login'),
  register('logout'),
  register('password'),
  register('project_add'),
  register('project_management'),
  register('restore'),
  register('role_add'),
  register('user_add'),
  register('user_deactivate'),
  register('user_management'),
  register('visibility'),
  register('visibility_off')

];


