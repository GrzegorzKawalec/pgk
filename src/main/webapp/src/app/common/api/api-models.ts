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

export enum Authority {
    ADMIN = "ADMIN",
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
    ROLE_CANNOT_UPDATE_ADMIN = "ROLE_CANNOT_UPDATE_ADMIN",
    ROLE_EMPTY_AUTHORITIES = "ROLE_EMPTY_AUTHORITIES",
    ROLE_NAME_EXISTS = "ROLE_NAME_EXISTS",
    ROLE_NOT_FOUND = "ROLE_NOT_FOUND",
    ROLE_SET_ADMIN_AUTHORITY = "ROLE_SET_ADMIN_AUTHORITY",
}
