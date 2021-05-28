package pl.gkawalec.pgk.common.type;

/**
 * These enum values are related to the dictionary (front-end) values (they are the keys).
 */

public enum ResponseExceptionType {

    // --- common ---
    ACCESS_DENIED,
    DATA_WAS_UPDATED_EARLIER,
    UNEXPECTED,


    EMPTY_DATA,

    // --- role  ---
    ROLE_BLANK_ID,
    ROLE_BLANK_NAME,
    ROLE_CANNOT_UPDATE_ADMIN,
    ROLE_EMPTY_AUTHORITIES,
    ROLE_NAME_EXISTS,
    ROLE_NOT_FOUND,
    ROLE_SET_ADMIN_AUTHORITY

}
