import {ActionTypes as types} from "../constants";

const defaultValue = {
    loaded: false,
    redirections: [],
    newRedirection: '',
    longUrl: '',
    shortUrl: 'Short Url will appear here!',
    isCopied: false
}

function redirection(state = defaultValue, action) {
    switch (action.type) {
        case types.RECEIVED_REDIRECTION_HISTORY_SUCCESS:
            return Object.assign({}, state, {
                loaded: true,
                redirections: action.data
            })
        case types.CHANGE_LONG_URL:
            return Object.assign({}, state, {
                longUrl: action.data.newLongUrl, isCopied: false, shortUrl: defaultValue.shortUrl
            })
        case types.CHANGE_COPIED:
            return Object.assign({}, state, {isCopied: action.data.isCopied})
        case types.RECEIVED_SHORTEN_REDIRECTION_SUCCESS:
            return Object.assign({}, state, {shortUrl: "http://localhost:4000/" + action.data.id})
        default:
            return state
    }
}

export default redirection