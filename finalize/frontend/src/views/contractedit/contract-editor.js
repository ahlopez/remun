import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@vaadin/vaadin-form-layout/src/vaadin-form-layout.js';
import '@vaadin/vaadin-form-layout/src/vaadin-form-item.js';
import '@vaadin/vaadin-combo-box/src/vaadin-combo-box.js';
import '@vaadin/vaadin-date-picker/src/vaadin-date-picker.js';
import '../../components/buttons-bar.js';
import '../../components/utils-mixin.js';
import './contract-parm-editor.js';
import '../../../styles/shared-styles.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';
class ContractEditor extends window.ScrollShadowMixin(PolymerElement) {
  static get template() {
    return html`
    <style include="shared-styles">
      :host {
        display: flex;
        flex-direction: column;
        flex: auto;
      }

      .meta-row {
        display: flex;
        justify-content: space-between;
        padding-bottom: var(--lumo-space-s);
      }

      .dim {
        color: var(--lumo-secondary-text-color);
        text-align: right;
        white-space: nowrap;
        line-height: 2.5em;
      }

      .status {
        width: 10em;
      }
    </style>

    <div class="scrollable flex1" id="main">
      <h2 id="title">Nuevo Contrato</h2>

      <div class="meta-row" id="metaContainer">
        <vaadin-combo-box class="fase" id="fase"></vaadin-combo-box>
        <span class="dim">Contrato <span id="code"></span></span>
        <vaadin-combo-box class="status" id="status"></vaadin-combo-box>
      </div>

      <vaadin-form-layout id="form1">

        <vaadin-form-layout id="form2">
          <vaadin-text-field id="name" label="Nombre" colspan="2">
          <vaadin-text-field id="contractor" label="Contratista" colspan="2">
          <vaadin-date-picker id="fromDate" label="Desde"></vaadin-date-picker>
          <vaadin-date-picker id="toDate" label="Hasta"></vaadin-date-picker>
        </vaadin-form-layout>

        <vaadin-form-layout id="form3" colspan="3">

          <vaadin-form-item colspan="3">
            <label slot="label">Parámetros</label>
          </vaadin-form-item>
          <div id="parmsContainer" colspan="3"></div>
        </vaadin-form-layout>

      </vaadin-form-layout>
    </div>

    <buttons-bar id="footer" no-scroll\$="[[noScroll]]">
      <vaadin-button slot="left" id="cancel">Cancelar</vaadin-button>
      <vaadin-button slot="right" id="review" theme="primary">
        Revisar
        <iron-icon icon="vaadin:arrow-right" slot="suffix"></iron-icon>
      </vaadin-button>
    </buttons-bar>
`;
  }

  static get is() {
    return 'contract-editor';
  }

  static get properties() {
    return {
      status: {
        type: String,
        observer: '_onStatusChange'
      }
    };
  }

  ready() {
    super.ready();

    // Not using attributes since Designer does not suppor single-quote attributes
    this.$.form1.responsiveSteps = [
      {columns: 1, labelsPosition: 'top'},
      {minWidth: '600px', columns: 4, labelsPosition: 'top'}
    ];
    this.$.form2.responsiveSteps = [
      {columns: 1, labelsPosition: 'top'},
      {minWidth: '360px', columns: 2, labelsPosition: 'top'}
    ];
    this.$.form3.responsiveSteps = [
      {columns: 1, labelsPosition: 'top'},
      {minWidth: '500px', columns: 3, labelsPosition: 'top'}
    ];
  }

  _onStatusChange() {
    const status = this.status ? this.status.toUpperCase() : this.status;
    this.$.status.$.input.setAttribute('status', status);

    const phase = this.phase ? this.phase.toUpperCase() : this.phase;
    this.$.phase.$.input.setAttribute('fase', phase);
  }
}//ContractEditor

window.customElements.define(ContractEditor.is, ContractEditor);
