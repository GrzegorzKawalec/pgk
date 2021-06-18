/* tslint:disable */
/* eslint-disable */

export interface AuditingDTO<T> {
    dto?: T;
    info?: AuditingInfoDTO;
}

export interface AuditingInfoDTO {
    created?: AuditorDTO;
    lastModified?: AuditorDTO;
}

export interface AuditorDTO {
    date?: string;
    email?: string;
    firstName?: string;
    id?: number;
    lastName?: string;
    time?: string;
}

export interface AuthorityDTO {
    authorities?: Authority[];
}

export interface BaseCriteria {
    searchBy?: string;
    searchPage?: SearchPageDTO;
}

export interface InfoBasicDTO {
    author?: string;
    name?: string;
    version?: string;
}

export interface RestExceptionDTO {
    errorUUID?: string;
    httpStatus?: number;
    type?: ResponseExceptionType;
}

export interface RoleAuditingDTO extends AuditingDTO<RoleDTO> {
    dto?: RoleDTO;
}

export interface RoleCriteria extends BaseCriteria {
    authorities?: Authority[];
}

export interface RoleDTO {
    authorities?: Authority[];
    description?: string;
    entityVersion?: number;
    id?: number;
    name?: string;
}

export interface SearchPageDTO {
    pageNumber?: number;
    pageSize?: number;
    sorting?: SortDTO[];
}

export interface SortDTO {
    direction?: Direction;
    property?: string;
}

export interface UserAuditingDTO extends AuditingDTO<UserUpsertDTO> {
    dto?: UserUpsertDTO;
}

export interface UserCriteria extends BaseCriteria {
    roleIds?: number[];
}

export interface UserDTO {
    authorities?: Authority[];
    description?: string;
    email?: string;
    entityVersion?: number;
    firstName?: string;
    id?: number;
    lastName?: string;
    phoneNumber?: string;
}

export interface UserSearchDTO {
    role?: RoleDTO;
    user?: UserDTO;
}

export interface UserUpsertDTO {
    password?: string;
    role?: RoleDTO;
    user?: UserDTO;
}

export enum Authority {
    ADMIN = "ADMIN",
    GUEST = "GUEST",
    ROLE_READ = "ROLE_READ",
    ROLE_WRITE = "ROLE_WRITE",
    USER_READ = "USER_READ",
    USER_WRITE = "USER_WRITE",
}

export enum Direction {
    ASC = "ASC",
    DESC = "DESC",
}

export enum ResponseExceptionType {
    ACCESS_DENIED = "ACCESS_DENIED",
    DATA_WAS_UPDATED_EARLIER = "DATA_WAS_UPDATED_EARLIER",
    UNEXPECTED = "UNEXPECTED",
    EMPTY_DATA = "EMPTY_DATA",
    ROLE_BLANK_ID = "ROLE_BLANK_ID",
    ROLE_BLANK_NAME = "ROLE_BLANK_NAME",
    ROLE_CANNOT_DELETE_ADMIN = "ROLE_CANNOT_DELETE_ADMIN",
    ROLE_CANNOT_DELETE_GUEST = "ROLE_CANNOT_DELETE_GUEST",
    ROLE_CANNOT_UPDATE_ADMIN = "ROLE_CANNOT_UPDATE_ADMIN",
    ROLE_CANNOT_UPDATE_GUEST = "ROLE_CANNOT_UPDATE_GUEST",
    ROLE_EMPTY_AUTHORITIES = "ROLE_EMPTY_AUTHORITIES",
    ROLE_NAME_EXISTS = "ROLE_NAME_EXISTS",
    ROLE_NOT_FOUND = "ROLE_NOT_FOUND",
    ROLE_SET_ADMIN_AUTHORITY = "ROLE_SET_ADMIN_AUTHORITY",
    ROLE_SET_GUEST_AUTHORITY = "ROLE_SET_GUEST_AUTHORITY",
    USER_BLANK_EMAIL = "USER_BLANK_EMAIL",
    USER_BLANK_FIRST_NAME = "USER_BLANK_FIRST_NAME",
    USER_BLANK_LAST_NAME = "USER_BLANK_LAST_NAME",
    USER_BLANK_ROLE_ID = "USER_BLANK_ROLE_ID",
    USER_BLANK_USER_ID = "USER_BLANK_USER_ID",
    USER_CANNOT_CHANGE_EMAIL = "USER_CANNOT_CHANGE_EMAIL",
    USER_CANNOT_CHANGE_PASSWORD = "USER_CANNOT_CHANGE_PASSWORD",
    USER_CANNOT_SET_ADMIN_ROLE = "USER_CANNOT_SET_ADMIN_ROLE",
    USER_CANNOT_UPDATE_ADMIN_ROLE = "USER_CANNOT_UPDATE_ADMIN_ROLE",
    USER_EMAIL_EXISTS = "USER_EMAIL_EXISTS",
    USER_EMPTY_PASSWORD = "USER_EMPTY_PASSWORD",
    USER_EMPTY_ROLE_DATA = "USER_EMPTY_ROLE_DATA",
    USER_EMPTY_USER_DATA = "USER_EMPTY_USER_DATA",
    USER_NOT_FOUND = "USER_NOT_FOUND",
}
