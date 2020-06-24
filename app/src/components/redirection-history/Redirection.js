import React from 'react'

class Redirection extends React.Component {

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

export default Redirection