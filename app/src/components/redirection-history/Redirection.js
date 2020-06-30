import React from 'react'
import {ActionTypes as types} from "../../constants";
import {connect} from 'react-redux'

class Redirection extends React.Component {

    componentDidMount() {
        let headers = {
            'Content-Type': 'application/json',
            "Authorization": this.props.tokenType + " " + this.props.accessToken
        }

        fetch("http://localhost:4000/user/analytics/" + this.props.data.id, {
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
    }

    render() {
        return (
            <tr>
                <td><a href={this.props.data.url}>{this.props.data.url}</a></td>
                <td>{this.props.data.longUrl}</td>
                <td>{new Date(this.props.data.createdAtMillis).toUTCString()}</td>
            </tr>
        );
    }
}

export default connect((state, props) => {
    return {
        tokenType: state.auth.tokenType,
        accessToken: state.auth.accessToken
    }
})(Redirection)