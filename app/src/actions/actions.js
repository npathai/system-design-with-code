import {ActionTypes as types} from "../constants";

export function changeCopied(isCopied) {
    return {
        type: types.CHANGE_COPIED,
        data: {isCopied: isCopied}
    }
}

export function createRedirection(token, longUrl) {
    return (dispatch) => {
        let headers = {
            'Content-Type': 'application/json',
            "Authorization": token.type + " " + token.accessToken
        }

        dispatch({type: types.REQUEST_SHORTEN_REDIRECTION, data: {longUrl: longUrl}})

        fetch("http://localhost:4000/shorten", {
            method: 'POST',
            headers: headers,
            mode: 'cors',
            body: JSON.stringify({
                longUrl: longUrl,
            })
        })
            .then(res => {
                if (res.status !== 200) {
                    throw new Error("Received response with status: " + res.status)
                }
                return res.json()
            })
            .then(data => {
                dispatch({type: types.RECEIVED_SHORTEN_REDIRECTION_SUCCESS, data: data})
                dispatch(fetchRedirectionHistory(token))
            })
            // We can do better error handling than this!
            .catch(err => {
                console.log(err)
                dispatch({type: types.RECEIVED_SHORTEN_REDIRECTION_FAILURE, data: {errorMessage: getErrorMessage(err)}})
            });
    }
}

function getErrorMessage(resp) {
    var msg = 'Error. Please try again later.'

    if (resp && resp.request && resp.request.status === 0) {
        msg = 'Oh no! App appears to be offline.'
    }

    return msg
}

export function changeLongUrl(newValue) {
    return {
        type: types.CHANGE_LONG_URL,
        data: {newLongUrl: newValue}
    }
}


export function fetchRedirectionHistory(token) {
    return (dispatch) => {
        let headers = {
            'Content-Type': 'application/json',
            "Authorization": token.type + " " + token.accessToken
        }

        dispatch({type: types.REQUEST_REDIRECTION_HISTORY, data: {}})

        fetch("http://localhost:4000/user/redirection_history", {
            method: 'GET',
            headers: headers,
            mode: 'cors'
        })
            .then(res => {
                if (res.status !== 200) {
                    throw new Error("Error in fetching redirection history")
                }
                return res.json()
            })
            .then(res => {
                console.log(res)
                dispatch({type: types.RECEIVED_REDIRECTION_HISTORY_SUCCESS, data: res})
            })
            .catch(err => {
                console.log(err)
                dispatch({type: types.RECEIVED_REDIRECTION_HISTORY_FAILURE, data: {errorMessage: getErrorMessage(err)}})
            })
    };
}


export function changeUsername(newUsername) {
    return {
        type: types.CHANGE_USERNAME,
        data: {newUsername: newUsername}
    }
}

export function changePassword(newPassword) {
    return {
        type: types.CHANGE_PASSWORD,
        data: {newPassword: newPassword}
    }
}

export function signIn(credentials) {
    return (dispatch) => {
        dispatch({type: types.REQUEST_SIGN_IN, data: credentials})

        fetch("http://localhost:4000/login", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            body: JSON.stringify({
                username: credentials.username,
                password: credentials.password
            })
        }).then(res => {
                if (res.status === 200) {
                    return res.json()
                }

                throw new Error("" + res.status)
            })
            .then(res => {
                dispatch({type: types.RECEIVED_SIGN_IN_SUCCESS, data: res})
            })
            .catch(err => {
                console.log(err)
                if (err.message === '401') {
                    dispatch({type: types.RECEIVED_SIGN_IN_INVALID_ATTEMPT_FAILURE, data: {}})
                } else {
                dispatch({type: types.RECEIVED_SIGN_IN_FAILURE, data: {errorMessage: getErrorMessage(err)}})
                }
            })
    }
}
