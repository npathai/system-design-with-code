import {ActionTypes as types} from '../constants'

const defaultValue = {
    errorMessage: ''
}

function error(state = defaultValue, action) {
    switch(action.type) {
        case types.RECEIVED_SIGN_IN_FAILURE:
        case types.RECEIVED_REDIRECTION_HISTORY_FAILURE:
        case types.RECEIVED_SHORTEN_REDIRECTION_FAILURE:
            return Object.assign({}, state, {errorMessage: action.data.errorMessage})
        case types.RECEIVED_SIGN_IN_INVALID_ATTEMPT_FAILURE:
        case types.RECEIVED_SIGN_IN_SUCCESS:
        case types.RECEIVED_SHORTEN_REDIRECTION_SUCCESS:
        case types.RECEIVED_SHORTEN_REDIRECTION_SUCCESS:
            return Object.assign({}, state, {errorMessage: defaultValue.errorMessage})
        default:
            return state
    }
}

export default error