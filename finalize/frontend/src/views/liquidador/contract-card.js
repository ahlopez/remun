import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import '../../../styles/shared-styles.js';
import './contract-status-badge.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';
class ContractCard extends PolymerElement
{
  static get template() {
    return html`
    <style include="shared-styles">
      :host {
        display: block;
      }

      .content {
        display: block;
        width: 100%;
        margin-left: auto;
        margin-right: auto;
      }

      .wrapper {
        background: var(--lumo-base-color);
        background-image: linear-gradient(var(--lumo-tint-5pct), var(--lumo-tint-5pct));
        box-shadow: 0 3px 5px var(--lumo-shade-10pct);
        border-bottom: 1px solid var(--lumo-shade-10pct);
        display: flex;
        padding: var(--lumo-space-l) var(--lumo-space-m);
        cursor: pointer;
      }

      .main {
        color: var(--lumo-secondary-text-color);
        margin-right: var(--lumo-space-s);
        font-weight: bold;
      }

      .group-heading {
        margin: var(--lumo-space-l) var(--lumo-space-m) var(--lumo-space-s);
      }

      .secondary {
        color: var(--lumo-secondary-text-color);
      }

      .info-wrapper {
        display: flex;
        flex-direction: column-reverse;
        justify-content: flex-end;
      }

      .badge {
        margin: var(--lumo-space-s) 0;
        width: 100px;
      }

      .time-place {
        width: 120px;
      }

      .name-items {
        flex: 1;
      }

      .place,
      .secondary-time,
      .full-day,
      .goods {
        color: var(--lumo-secondary-text-color);
      }

      .time,
      .name,
      .short-day,
      .month {
        margin: 0;
      }

      .name {
        word-break: break-all;
        /* Non standard for WebKit */
        word-break: break-word;
        white-space: normal;
      }

      .parms {
        display: flex;
        flex-wrap: wrap;
      }

      .parms > div {
        box-sizing: border-box;
        width: 18em;
        flex: auto;
        padding-right: var(--lumo-space-l);
      }

      .parm-item {
        display: flex;
        align-items: baseline;
        font-size: var(--lumo-font-size-s);
        margin: var(--lumo-space-xs) 0;
      }

      .parm-item > .count {
        margin-right: var(--lumo-space-s);
        white-space: nowrap;
      }

      .parm-item > div {
        flex: auto;
        word-break: break-all;
        /* Non standard for WebKit */
        word-break: break-word;
        white-space: normal;
      }

      @media (min-width: 600px) {
        .info-wrapper {
          flex-direction: row;
        }

        .wrapper {
          border-radius: var(--lumo-border-radius);
        }

        .badge {
          margin: 0;
        }

        .content {
          max-width: 964px;
        }
      }
    </style>
    <div class="content">
      <div class="group-heading" hidden\$="[[!header]]">
        <span class="main">[[header.main]]</span>
        <span class="secondary">[[header.secondary]]</span>
      </div>
      <div class="wrapper" on-click="_cardClick">
        <div class="info-wrapper">
          <contract-status-badge class="badge" status="[[contractCard.state]]"></contract-status-badge>
          <div class="time-place">
            <h3 class="code">[[contractCard.code]]</h3>
            <h3 class="name">[[contractCard.name]]</h3>
            <h3 class="phase">[[contractCard.phase]]</h3>
            <h3 class="contractor">[[contractCard.contractor]]</h3>
            <h3 class="from-date">[[contractCard.fromDate]]</h3>
            <h3 class="to-date">[[contractCard.toDate]]</h3>
            <div class="status">[[contractCard.state]]</div>
          </div>
        </div>
        <div class="name-items">
          <h3 class="name">[[contractCard.fullName]]</h3>
          <div class="parms">
            <template is="dom-repeat" items="[[contractCard.parms]]">
              <div class="parms-item">
                <span class="count">[[item.quantity]]</span>
                <div>[[item.product.name]]</div>
              </div>
            </template>
          </div>
        </div>
      </div>
    </div>
`;
  }

  static get is() {
    return 'contract-card';
  }

  _cardClick() {
    this.dispatchEvent(new CustomEvent('card-click'));
  }
}

window.customElements.define(ContractCard.is, ContractCard);
