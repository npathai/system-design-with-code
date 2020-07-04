import React from 'react'
import {connect} from 'react-redux'
import * as actions from '../../actions/actions'

class Shortener extends React.Component {

    constructor(props) {
        super(props);
        this.shortUrlText = React.createRef();
        this.handleLongUrlChange = this.handleLongUrlChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.copyToClipboard = this.copyToClipboard.bind(this);
    }

    handleLongUrlChange(event) {
        this.props.dispatch(actions.changeLongUrl(event.target.value))
    }

    isValidHttpUrl(string) {
        let url;
        try {
            url = new URL(string)
        } catch (_) {
            return false
        }
        return url.protocol === 'http:' || url.protocol === 'https:'
    }

    handleSubmit(event) {
        const token = {
            type: this.props.tokenType,
            accessToken: this.props.accessToken
        }
        this.props.dispatch(actions.createRedirection(this.props.isLoggedIn, token, this.props.longUrl))
        event.preventDefault();
    }

    copyToClipboard() {
        this.shortUrlText.current.select();
        document.execCommand("copy");
        this.props.dispatch(actions.changeCopied(true))
        // this.setState({isCopied: true})
    }

    render() {

        return (<div className="row justify-content-center padding">
                <div className="col-md-8 ftco-animate fadeInUp ftco-animated">
                    <form id="form1" onSubmit={this.handleSubmit} className="domain-form">
                        <div className="form-group d-md-flex">
                            <input type="text"
                                   id="longUrl"
                                   value={this.props.longUrl}
                                   onChange={this.handleLongUrlChange}
                                   className="form-control px-4"
                                   placeholder="Enter URL..." required/>
                            <input type="submit" className="search-domain btn btn-primary px-5" value="Shorten" readOnly={true}/>
                        </div>
                    </form>

                    <div className="form-group d-md-flex">
                        <input type="text" id="shortUrl" value={this.props.shortUrl}
                               className="form-control px-4" ref={this.shortUrlText} readOnly={true}/>
                        <input type="button" className="search-domain btn btn-primary px-5" value={
                            this.props.isCopied ? "Copied!" : "Copy"
                        } onClick={this.copyToClipboard}/>
                    </div>

                </div>
            </div>
        );
    }
}

export default connect((state, props) => {
    return {
        isLoggedIn: state.auth.isLoggedIn,
        tokenType: state.auth.tokenType,
        accessToken: state.auth.accessToken,
        shortUrl: state.redirection.shortUrl,
        longUrl: state.redirection.longUrl,
        isCopied: state.redirection.isCopied
    }
})(Shortener)