import React from 'react'
import {connect} from 'react-redux'
import Redirection from "./Redirection";
import * as actions from '../../actions/actions'

class RedirectionHistory extends React.Component {

    componentDidMount() {
        const token = {
            type: this.props.tokenType,
            accessToken: this.props.accessToken
        }

        this.props.dispatch(actions.fetchRedirectionHistory(token))
    }

    render() {

        const redirectionElements = this.props.loaded ? this.props.redirections.map(each => {
                each['url'] = "http://localhost:4000/" + each.id;
                return <Redirection key={each.id} data={each}/>
        }) : null;

        return this.props.loaded ?
            (
                <>
                    <div className="content row justify-content-center">
                        <h5>{this.props.redirections.length} redirections</h5>
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

export default connect((state, props) => {
    console.log('state', state)
    return {
        tokenType: state.auth.tokenType,
        accessToken: state.auth.accessToken,
        loaded: state.redirection.loaded,
        redirections: state.redirection.redirections
    }
})(RedirectionHistory)