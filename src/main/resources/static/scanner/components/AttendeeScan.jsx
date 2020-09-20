import React from "react";

const titleMessages = {
    not_found: "Barcode niet gevonden.",
    duplicate: "Reeds gescand.",
    requested: "In verwerking.",
    confirmed: "Gescand.",
    error: "Er ging iets mis."
};

class AttendeeScan extends React.PureComponent {
    render() {
        let status = this.props.scan.status;

        let name;
        if(this.props.scan.result && this.props.scan.result.attendee) {
            const attendee = this.props.scan.result.attendee;
            name = `${ attendee.firstName } ${ attendee.lastName }`;
        } else {
            name = this.props.scan.barcode;
        }

        return <div className={`scan-item status-${status}`}>
            <h1>
                { name }
            </h1>
            <h3>
                {titleMessages[status]}
            </h3>
        </div>;
    }
}

export default AttendeeScan;
