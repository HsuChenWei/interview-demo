package com.interview.demo.error;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum ApiErrorCode {

    USER_NOT_FOUND,
    USER_DISABLED,
    USER_LOCKED,
    BAD_CREDENTIAL,
    INVALID_REFRESH_TOKEN,
    INVALID_AUTHORIZATION_CODE,
    USER_EXISTED,
    BLOB_NOT_FOUND,
    FORBIDDEN,
    UNAUTHORIZED,
    UNEXPECTED_ERROR,
    INVALID_MOBILE_FORMAT,
    INVALID_OTP_VERIFY_STATE,
    OTP_VERIFY_EXPIRED,
    INVALID_OTP,
    INVALID_PASSWORD,
    INVALID_RESEND_FREQUENCY,
    INVALID_UPDATE_TYPE,
    USER_UPDATE_TICKET_NOT_FOUND, REFRESH_EXPIRED,
    INVALID_REFRESH_CODE,
    ACTION_TICKET_NOT_FOUND,
    PARENT_DEPT_NOT_FOUND,
    INCNSISTENT_COMPANY,
    DEPT_NOT_FOUND,
    USER_NOT_DELETABLE,
    INVALID_DEPT_ADMIN_STATE,
    DEPT_MEMBER_NOT_FOUND,
    INVALID_COMPANY_ADMIN_STATE,
    COMPANY_NOT_FOUND,
    GROUP_NOT_FOUND,
    GROUP_NOT_MATCHED,
    GROUP_MEMBER_NOT_FOUND,
    APPROVAL_NOT_FOUND,
    INVALID_APPROVAL_ACTION,
    COMPANY_CREATE_FAILED,
    DEPT_CREATE_FAILED,
    INVALID_APPROVAL_DETAIL,
    DEPT_ALREADY_BIND_TO_OTHER_COMPANY,
    COMPANY_ALREADY_BIND_TO_OTHER_GROUP,
    INVALID_ATTACHMENT_URL,
    ADMIN_ALREADY_CHANGED,
    MOBILE_EXISTED,
    APPROVAL_EXPIRED,
    INVALID_APPROVAL_STATUS,
    INVALID_GROUP_ADMIN_STATE,
    GROUP_STILL_HAS_COMPANY,
    COMPANY_STILL_HAS_DEPT,
    CONFLICT_PENDING_APPROVAL_EXISTED,
    REDUNDANT_APPROVAL_REQUEST,
    SELF_APPROVING_REQUEST,
    GROUP_NOT_PROVIDED,
    CONFLICT_PARENT_DEPT,
    COMPANY_NOT_PROVIDED,
    CYCLIC_DEPT_LINK,
    INVALID_DEPT_ADMIN,
    COMPANY_MEMBER_NOT_FOUND,
    TASK_NOT_FOUND,
    POST_NOT_FOUND,
    INVALID_ATTACHMENT_TYPE,
    POST_CANNOT_BE_DELETE_AFTER_REVIEWED,
    REACTION_NOT_FOUND,
    INVALID_OKR_DATE_TIME,
    OKR_REVIEWER_NOT_FOUND,
    INVALID_OKR_REVIEWER,
    ATTACHMENT_BLOB_NOT_PROVIDED,
    KEY_RESULT_NOT_FOUND,
    INVALID_OKR_STATUS,
    INVALID_BLOB_CONTENT_TYPE,
    POST_PARAGRAPH_NOT_FOUND,
    OKR_NOT_FOUND,
    COMPANY_HAS_NO_GROUP,
    OKR_REPORT_NOT_PROVIDED,
    KEY_RESULT_REPORT_NOT_PROVIDED,
    OKR_EVAL_NOT_COMPLETED,
    KR_EVAL_NOT_COMPLETED,
    INVALID_UPDATE_PERMISSION,
    FAQ_NOT_FOUND,
    FAQ_CATEGORY_NOT_FOUND,
    CAL_DAY_NOT_FOUND,
    ZONE_NOT_FOUND,
    START_DATE_AFTER_END_DATE,
    START_TIME_AFTER_END_TIME,

    START_DATE_NOT_IN_THE_SAME_YEAR,
    POLICY_NOT_FOUND,
    MEMBER_NOT_FOUND,
    ROLE_NOT_FOUND,
    INVALID_PERM_ID,
    INVALID_COMBINATION_TYPE,
    PRODUCT_NOT_FOUND,
    GRADE_NOT_FOUND,
    PERM_NOT_FOUND,
    SPEC_NOT_FOUND,
    COMBINATION_NOT_FOUND,
    FLAVOUR_NOT_FOUND,
    FLAVOUR_TYPE_NOT_FOUND,
    SIDE_TYPE_NOT_FOUND,
    UNIT_TYPE_NOT_FOUND,
    INVALID_DEFAULT_UNIT_ID,
    NOT_RELATED_USER, ORK_ALREADY_DUE,UNIT_NOT_FOUND,
    BOOKING_NOT_FOUND, PASSWORD_EXISTED, USERNAME_OR_PASSWORD_ERROR;

}
