import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            longUrl: "",
            shortUrl: "Short Url will appear here!",
            copySuccess: false
        };
        this.shortUrlText = React.createRef();
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.copyToClipboard = this.copyToClipboard.bind(this);
    }

    handleChange(event) {
        this.setState({longUrl: event.target.value});
    }

    handleSubmit(event) {
        this.postLongUrl();
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
    }

    copyToClipboard() {
        this.shortUrlText.current.select();
        document.execCommand("copy");
        this.setState({copySuccess: true})
    }

    render() {
        return (<div className="row justify-content-center padding">
                <div className="col-md-8 ftco-animate fadeInUp ftco-animated">
                    <form id="form1" onSubmit={this.handleSubmit} className="domain-form">
                        <div className="form-group d-md-flex">
                            <input type="text" id="longUrl" value={this.state.longUrl} onChange={this.handleChange}
                                   className="form-control px-4" placeholder="Enter URL..."/>
                            <input type="submit" className="search-domain btn btn-primary px-5" value="Shorten"/>
                        </div>
                    </form>

                    <div className="form-group d-md-flex">
                        <input type="text" id="shortUrl" value={this.state.shortUrl} disabled={true}
                               className="form-control px-4" ref={this.shortUrlText}/>
                        <input type="button" className="search-domain btn btn-primary px-5" value={
                            this.state.copySuccess ? "Ain't working!" : "Copy"
                        } onClick={this.copyToClipboard}/>
                    </div>

                </div>
            </div>
        );
    }
}

ReactDOM.render(
    <App/>, document.getElementById('root')
);
