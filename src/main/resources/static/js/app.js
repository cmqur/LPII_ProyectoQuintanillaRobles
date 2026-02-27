// Small UI helpers (Bootstrap 5)
(function () {
  'use strict';

  // Generic table filter: add data-table-filter-target="#tableId" to an input
  document.querySelectorAll('[data-table-filter-target]').forEach(function (input) {
    var targetSelector = input.getAttribute('data-table-filter-target');
    var table = document.querySelector(targetSelector);
    if (!table) return;

    input.addEventListener('input', function () {
      var q = (input.value || '').toLowerCase().trim();
      table.querySelectorAll('tbody tr').forEach(function (row) {
        var text = (row.innerText || '').toLowerCase();
        // Keep "empty" placeholder rows visible if they span many columns
        var isEmptyRow = row.querySelector('td[colspan]') !== null;
        if (isEmptyRow) {
          row.style.display = q ? 'none' : '';
          return;
        }
        row.style.display = text.indexOf(q) !== -1 ? '' : 'none';
      });
    });
  });

  // Movement form UX: show/hide origin/destination based on tipo
  var movementRoot = document.querySelector('[data-movement-form]');
  if (movementRoot) {
    var tipo = movementRoot.querySelector('#tipoMovimiento');
    var origen = movementRoot.querySelector('[data-only="origen"]');
    var destino = movementRoot.querySelector('[data-only="destino"]');

    var kTipo = 'mov.tipo';
    var kOri = 'mov.origen';
    var kDes = 'mov.destino';
    var kUsr = 'mov.user';

    var selOrigen = movementRoot.querySelector('#almacenOrigen');
    var selDestino = movementRoot.querySelector('#almacenDestino');
    var selUsuario = movementRoot.querySelector('#idUsuario');

    function applyVisibility() {
      var v = (tipo && tipo.value) ? tipo.value : '';

      if (origen) origen.style.display = (v === 'SALIDA' || v === 'TRANSFERENCIA') ? '' : 'none';
      if (destino) destino.style.display = (v === 'INGRESO' || v === 'TRANSFERENCIA') ? '' : 'none';

      // Required hints (lightweight):
      if (selOrigen) selOrigen.required = (v === 'TRANSFERENCIA');
      if (selDestino) selDestino.required = (v === 'INGRESO' || v === 'TRANSFERENCIA');
    }

    function saveDraft() {
      try {
        localStorage.setItem(kTipo, tipo ? (tipo.value || '') : '');
        localStorage.setItem(kOri, selOrigen ? (selOrigen.value || '') : '');
        localStorage.setItem(kDes, selDestino ? (selDestino.value || '') : '');
        localStorage.setItem(kUsr, selUsuario ? (selUsuario.value || '') : '');
      } catch (e) {}
    }

    function loadDraft() {
      try {
        if (tipo && !tipo.value) tipo.value = localStorage.getItem(kTipo) || '';
        if (selOrigen && !selOrigen.value) selOrigen.value = localStorage.getItem(kOri) || '';
        if (selDestino && !selDestino.value) selDestino.value = localStorage.getItem(kDes) || '';
        if (selUsuario && !selUsuario.value) selUsuario.value = localStorage.getItem(kUsr) || '';
      } catch (e) {}
    }

    loadDraft();
    applyVisibility();

    ['change', 'input'].forEach(function (evt) {
      if (tipo) tipo.addEventListener(evt, function () { applyVisibility(); saveDraft(); });
      if (selOrigen) selOrigen.addEventListener(evt, saveDraft);
      if (selDestino) selDestino.addEventListener(evt, saveDraft);
      if (selUsuario) selUsuario.addEventListener(evt, saveDraft);
    });
  }
})();
