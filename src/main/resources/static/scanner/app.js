const React = require('react');
const ReactDOM = require('react-dom');
import Scanner from "./components/Scanner";

const csrfToken = $("meta[name='_csrf']").attr("content");
ReactDOM.render(
    <Scanner csrfToken={csrfToken} />,
    document.getElementById('ugent-scanner')
)