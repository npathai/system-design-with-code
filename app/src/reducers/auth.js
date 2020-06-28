import {ActionTypes as types} from "../constants";

const defaultValue = {
    username: '',
    password: '',
    isLoggedIn: false,
    isLoginAttemptFailure: false,
    accessToken: undefined,
    refreshToken: undefined,
    tokenType: undefined
}

function auth(state = defaultValue, action) {
    switch (action.type) {
        case types.CHANGE_USERNAME:
            return Object.assign({}, state, {username: action.data.newUsername})
        case types.CHANGE_PASSWORD:
            return Object.assign({}, state, {password: action.data.newPassword})
        case types.RECEIVED_SIGN_IN_SUCCESS:
            return Object.assign({}, state, {isLoggedIn: true, password: defaultValue.password,
                accessToken: action.data.access_token, refreshToken: action.data.refresh_token,
                tokenType: action.data.token_type})
        case types.RECEIVED_SIGN_IN_FAILURE:
            return Object.assign({}, state, {isLoggedIn: false, password: defaultValue.password,
                isLoginAttemptFailure: false})
        case types.RECEIVED_SIGN_IN_INVALID_ATTEMPT_FAILURE:
            return Object.assign({}, state, {isLoggedIn: false, password: defaultValue.password,
                isLoginAttemptFailure: true})
        default:
            return state
    }
}

export default auth;

