import {ActionTypes as types} from "../constants";

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
