import keyMirror from 'keymirror'


export const ActionTypes = keyMirror({
    CHANGE_USERNAME: null,
    CHANGE_PASSWORD: null,

    REQUEST_SIGN_IN: null,
    RECEIVED_SIGN_IN_SUCCESS: null,
    RECEIVED_SIGN_IN_FAILURE: null,
    REQUEST_REDIRECTION_HISTORY: null,
    RECEIVED_REDIRECTION_HISTORY_SUCCESS: null,
    RECEIVED_REDIRECTION_HISTORY_FAILURE: null
})