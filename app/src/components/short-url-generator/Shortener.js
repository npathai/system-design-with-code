import React from 'react'

class Shortener extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            longUrl: "",
            shortUrl: "Short Url will appear here!",
            isCopied: false,
            valid: false
        };
        this.shortUrlText = React.createRef();
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.copyToClipboard = this.copyToClipboard.bind(this);
    }

    handleChange(event) {
        this.setState({longUrl: event.target.value});
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
        if (this.isValidHttpUrl(this.state.longUrl)) {
            this.setState({valid: true})
            this.setState({isCopied: false})
            this.postLongUrl();
        } else {
            this.setState({valid: false})
        }
        event.preventDefault();
    }

    postLongUrl() {
        fetch("http://localhost:4000/shorten", {
            method: 'POST',
            mode: 'cors',
            body: JSON.stringify({
                longUrl: this.state.longUrl,
            })
        })
            .then(res => res.json())
            .then(data => this.setState({shortUrl: "http://localhost:4000/" + data.id}))
            // We can do better error handling than this!
            .catch(err => console.log(err))
    }

    copyToClipboard() {
        this.shortUrlText.current.select();
        document.execCommand("copy");
        this.setState({isCopied: true})
    }

    render() {

        return (<div className="row justify-content-center padding">
                <div className="col-md-8 ftco-animate fadeInUp ftco-animated">
                    <form id="form1" onSubmit={this.handleSubmit} className="domain-form">
                        <div className="form-group d-md-flex">
                            <input type="text"
                                   id="longUrl"
                                   value={this.state.longUrl}
                                   onChange={this.handleChange}
                                   className="form-control px-4"
                                   placeholder="Enter URL..." required/>
                            <input type="submit" className="search-domain btn btn-primary px-5" value="Shorten" readOnly={true}/>
                        </div>
                    </form>

                    <div className="form-group d-md-flex">
                        <input type="text" id="shortUrl" value={this.state.shortUrl}
                               className="form-control px-4" ref={this.shortUrlText} readOnly={true}/>
                        <input type="button" className="search-domain btn btn-primary px-5" value={
                            this.state.isCopied ? "Copied!" : "Copy"
                        } onClick={this.copyToClipboard}/>
                    </div>

                </div>
            </div>
        );
    }
}

export default Shortener