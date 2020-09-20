$(function () {
    const scroll = document.querySelector(".scroll");
    if(scroll) {
        scroll.scrollIntoView();
    }

    const $invoiceUploadForm = $("#bulk-invite-upload-form");
    $invoiceUploadForm.submit(function() {
        const payload = new FormData($(this)[0]);
        const $target = $("#stream-output");
        let numberOfRecords = 0;
        let numberOfProcessedRecords = 0;
        let source = new SSE($(this).prop("action"), { payload: payload });
        const $output = $("<div.list-group>");
        $target.append($output);
        const recordCount = $("#record-count");
        source.addEventListener('message', function(e) {
            const payload = JSON.parse(e.data);
            switch (payload.type) {
                case "INVALID_CSV":
                    $invoiceUploadForm.hide();
                    $target.show();
                    $target.empty();
                    $target.text("CSV kon niet correct ingelezen worden.");
                    break;
                case "METADATA":
                    $invoiceUploadForm.hide();
                    $target.show();
                    numberOfRecords = payload.records;
                    break;
                case "INVALID_INVITE":
                    const $invalidInvite = $("<div>", { class: "list-group-item" });
                    const $title = $("<h4>", { class: "list-group-item-heading" });
                    $title.text('Kon uitnodiging voor '+ (payload.invite.email || payload.invite.username) + ' niet inlezen.');
                    $invalidInvite.append($title);

                    $output.append($invalidInvite);
                    ++numberOfProcessedRecords;
                    break
                case "INVITE_RESULT":
                    ++numberOfProcessedRecords;

                    if(payload.result === "SUCCESS") {
                        break
                    }

                    const $incorrectInvite = $("<div>", { class: "list-group-item" });
                    const $incorrectInviteTitle = $("<h4>", { class: "list-group-item-heading" });
                    $incorrectInviteTitle.text('Kon uitnodiging voor '+ (payload.invite.email || payload.invite.username) + ' niet verwerken. Reden: ' + payload.result);
                    $incorrectInvite.append($incorrectInviteTitle);

                    $output.append($incorrectInvite);
            }

            recordCount.text(numberOfProcessedRecords + "/" + numberOfRecords)
        });
        source.stream();
        return false;
    });
});


/**
 * Copyright (C) 2016 Maxime Petazzoni <maxime.petazzoni@bulix.org>.
 * All rights reserved.
 */

var SSE = function (url, options) {
    if (!(this instanceof SSE)) {
        return new SSE(url, options);
    }

    this.INITIALIZING = -1;
    this.CONNECTING = 0;
    this.OPEN = 1;
    this.CLOSED = 2;

    this.url = url;

    options = options || {};
    this.headers = options.headers || {};
    this.payload = options.payload !== undefined ? options.payload : '';
    this.method = options.method || (this.payload && 'POST' || 'GET');

    this.FIELD_SEPARATOR = ':';
    this.listeners = {};

    this.xhr = null;
    this.readyState = this.INITIALIZING;
    this.progress = 0;
    this.chunk = '';

    this.addEventListener = function(type, listener) {
        if (this.listeners[type] === undefined) {
            this.listeners[type] = [];
        }

        if (this.listeners[type].indexOf(listener) === -1) {
            this.listeners[type].push(listener);
        }
    };

    this.removeEventListener = function(type, listener) {
        if (this.listeners[type] === undefined) {
            return;
        }

        var filtered = [];
        this.listeners[type].forEach(function(element) {
            if (element !== listener) {
                filtered.push(element);
            }
        });
        if (filtered.length === 0) {
            delete this.listeners[type];
        } else {
            this.listeners[type] = filtered;
        }
    };

    this.dispatchEvent = function(e) {
        if (!e) {
            return true;
        }

        e.source = this;

        var onHandler = 'on' + e.type;
        if (this.hasOwnProperty(onHandler)) {
            this[onHandler].call(this, e);
            if (e.defaultPrevented) {
                return false;
            }
        }

        if (this.listeners[e.type]) {
            return this.listeners[e.type].every(function(callback) {
                callback(e);
                return !e.defaultPrevented;
            });
        }

        return true;
    };

    this._setReadyState = function (state) {
        var event = new CustomEvent('readystatechange');
        event.readyState = state;
        this.readyState = state;
        this.dispatchEvent(event);
    };

    this._onStreamFailure = function(e) {
        this.dispatchEvent(new CustomEvent('error'));
        this.close();
    }

    this._onStreamProgress = function(e) {
        if (this.xhr.status !== 200) {
            this._onStreamFailure(e);
            return;
        }

        if (this.readyState == this.CONNECTING) {
            this.dispatchEvent(new CustomEvent('open'));
            this._setReadyState(this.OPEN);
        }

        var data = this.xhr.responseText.substring(this.progress);
        this.progress += data.length;
        data.split(/(\r\n|\r|\n){2}/g).forEach(function(part) {
            if (part.trim().length === 0) {
                this.dispatchEvent(this._parseEventChunk(this.chunk.trim()));
                this.chunk = '';
            } else {
                this.chunk += part;
            }
        }.bind(this));
    };

    this._onStreamLoaded = function(e) {
        this._onStreamProgress(e);

        // Parse the last chunk.
        this.dispatchEvent(this._parseEventChunk(this.chunk));
        this.chunk = '';
    };

    /**
     * Parse a received SSE event chunk into a constructed event object.
     */
    this._parseEventChunk = function(chunk) {
        if (!chunk || chunk.length === 0) {
            return null;
        }

        var e = {'id': null, 'retry': null, 'data': '', 'event': 'message'};
        chunk.split(/\n|\r\n|\r/).forEach(function(line) {
            line = line.trimRight();
            var index = line.indexOf(this.FIELD_SEPARATOR);
            if (index <= 0) {
                // Line was either empty, or started with a separator and is a comment.
                // Either way, ignore.
                return;
            }

            var field = line.substring(0, index);
            if (!(field in e)) {
                return;
            }

            var value = line.substring(index + 1).trimLeft();
            if (field === 'data') {
                e[field] += value;
            } else {
                e[field] = value;
            }
        }.bind(this));

        var event = new CustomEvent(e.event);
        event.data = e.data;
        event.id = e.id;
        return event;
    };

    this._checkStreamClosed = function() {
        if (this.xhr.readyState === XMLHttpRequest.DONE) {
            this._setReadyState(this.CLOSED);
        }
    };

    this.stream = function() {
        this._setReadyState(this.CONNECTING);

        this.xhr = new XMLHttpRequest();
        this.xhr.addEventListener('progress', this._onStreamProgress.bind(this));
        this.xhr.addEventListener('load', this._onStreamLoaded.bind(this));
        this.xhr.addEventListener('readystatechange', this._checkStreamClosed.bind(this));
        this.xhr.addEventListener('error', this._onStreamFailure.bind(this));
        this.xhr.addEventListener('abort', this._onStreamFailure.bind(this));
        this.xhr.open(this.method, this.url);
        for (var header in this.headers) {
            this.xhr.setRequestHeader(header, this.headers[header]);
        }
        this.xhr.send(this.payload);
    };

    this.close = function() {
        if (this.readyState === this.CLOSED) {
            return;
        }

        this.xhr.abort();
        this.xhr = null;
        this._setReadyState(this.CLOSED);
    };
};

// Export our SSE module for npm.js
if (typeof exports !== 'undefined') {
    exports.SSE = SSE;
}