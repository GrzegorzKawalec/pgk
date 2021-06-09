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
    ROLE_CANNOT_DELETE_ADMIN,
    ROLE_CANNOT_UPDATE_ADMIN,
    ROLE_EMPTY_AUTHORITIES,
    ROLE_NAME_EXISTS,
    ROLE_NOT_FOUND,
    ROLE_SET_ADMIN_AUTHORITY,

    // --- user ---
    USER_BLANK_EMAIL,
    USER_BLANK_FIRST_NAME,
    USER_BLANK_LAST_NAME,
    USER_BLANK_ROLE_ID,
    USER_BLANK_USER_ID,
    USER_CANNOT_CHANGE_EMAIL,
    USER_CANNOT_CHANGE_PASSWORD,
    USER_CANNOT_SET_ADMIN_ROLE,
    USER_CANNOT_UPDATE_ADMIN_ROLE,
    USER_EMAIL_EXISTS,
    USER_EMPTY_PASSWORD,
    USER_EMPTY_ROLE_DATA,
    USER_EMPTY_USER_DATA,
    USER_NOT_FOUND

}
