import React from 'react'
import {AuthContext} from "../../context/AuthContext";
import Redirection from "./Redirection";

class RedirectionHistory extends React.Component {
    static contextType = AuthContext

    constructor(props) {
        super(props);
        this.state = {
            loaded: false,
            redirections: []
        }
    }

    componentDidMount() {
        const headers = {
            'Content-Type': 'application/json'
        }
        const {addAuthorizationHeader} = this.context
        addAuthorizationHeader(headers)

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
            .then(data => {
                console.log(data)
                this.setState({
                    loaded: true,
                    redirections: data
                })
            })
            .catch(err => {
                console.log(err)
                this.setState({
                    loaded: false
                })
            })
    }

    render() {
        const {isLoggedIn} = this.context
        const redirectionElements = this.state.redirections.map(each => {
                each['url'] = "http://localhost:4000/" + each.id;
                return <Redirection key={each.id} data={each}/>
        })
        return isLoggedIn && this.state.loaded ?
            (
                <>
                    <div className="content row justify-content-center">
                        <h5>{this.state.redirections.length} redirections</h5>
                    </div>
                    <div className="content row justify-content-center">
                        <div className="row">
                            <table className = "table" style={{width: "inherit"}}>
                                <tbody>
                                    {redirectionElements}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </>
            ) : null
    }
}

export default RedirectionHistory