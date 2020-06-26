import {ActionTypes as types} from "../constants";

export function fetchRedirectionHistory(token) {
    return (dispatch) => {
        const headers = {
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
                dispatch({type: types.RECEIVED_REDIRECTION_HISTORY_FAILURE, data: {}})
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
        })
            .then(res => {
                if (res.status !== 200) {
                    throw new Error("Invalid login attempt")
                }
                return res.json()
            })
            .then(res => {
                dispatch({type: types.RECEIVED_SIGN_IN_SUCCESS, data: res})
            })
            .catch(err => {
                console.log(err)
                dispatch({type: types.RECEIVED_SIGN_IN_FAILURE, data: {}})
            })
    }
}
