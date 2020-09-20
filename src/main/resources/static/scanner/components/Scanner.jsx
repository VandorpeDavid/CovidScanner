import React from "react";
import {v4 as uuid} from "uuid";
import AttendeeScan from "./AttendeeScan";
import axios from "axios";
import qs from 'querystring';
import autoBind from 'react-autobind';

class Scanner extends React.PureComponent {
    constructor(props) {
        super(props);

        this.state = {barcode: "", scans: []};
        autoBind(this);
    }

    setBarcode(e) {
        this.setState({
            barcode: e.target.value
        });
    }

    updateScan(newScan) {
        const id = newScan.id;
        console.log(this);
        this.setState((state) => {
            let scans = state.scans;
            console.log("state", state);
            console.log("scans", scans, newScan);
            // Make copy
            scans = [...scans];
            const index = scans.findIndex((scan) => scan.id === id);
            if (index === -1) {
                console.error(`Index not found. Id: ${id}. Barcode: ${newScan.barcode}.`)
                return;
            }

            scans[index] = {
                ...scans[index],
                ...newScan
            }
            console.log(scans, scans[index]);

            return { scans };
        });
    }

    scanBarcode(e) {
        const id = uuid();
        console.log("this", this);

        const barcode = this.state.barcode;
        let scanRequest = {
            id: id,
            barcode,
            status: 'requested'
        };

        const _this = this;

        this.setState(
            {
                scans: [scanRequest, ...this.state.scans],
                barcode: ""
            },
            () => {
                axios.post(
                    '',
                    qs.stringify({
                        barcode: barcode,
                        _csrf: this.props.csrfToken
                    }),
                    {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    })
                    .then((response) => {
                        console.log("response", response.data);

                        let status = 'confirmed';

                        if (response.data.duplicate) {
                            status = 'duplicate';
                        }

                        this.updateScan({
                            result: response.data,
                            id,
                            status,
                            barcode
                        });
                    })
                    .catch((error) => {
                        console.log("error", error);
                        let status = 'error';
                        if (error.response.status === 404) {
                            status = 'not_found';
                        }

                        this.updateScan({
                            id,
                            status,
                            barcode
                        })
                    })
            }
        );
        e.preventDefault();
        return false;
    }

    render() {
        return <React.Fragment>
            <form onSubmit={this.scanBarcode.bind(this)} className="form-horizontal">
                <div className="form-group">
                    <label className="col-md-2 control-label" htmlFor="regular">Scan barcode</label>
                    <div className="col-md-10">
                        <input type="text" className="form-control" value={this.state.barcode}
                               onChange={this.setBarcode.bind(this)}/>
                    </div>
                </div>
            </form>
            <div id="scan-list">
                {
                    this.state.scans.map((scan) => <AttendeeScan scan={scan} key={scan.id}/>)
                }
            </div>
        </React.Fragment>;
    }
}

export default Scanner;