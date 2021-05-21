/* tslint:disable */
/* eslint-disable */

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
}

export enum ResponseExceptionType {
    UNEXPECTED = "UNEXPECTED"
}
