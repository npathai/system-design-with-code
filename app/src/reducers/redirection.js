import {ActionTypes as types} from "../constants";

const defaultValue = {
    loaded: false,
    redirections: []
}

function redirection(state = defaultValue, action) {
    switch (action.type) {
        case types.RECEIVED_REDIRECTION_HISTORY_SUCCESS:
            return Object.assign({}, state, {
                loaded: true,
                redirections: action.data
            })
        default:
            return state
    }
}

export default redirection