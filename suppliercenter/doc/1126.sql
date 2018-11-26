use zm_supplier;

alter table zm_supplier.supplier_base add contract_type tinyint Unsigned DEFAULT 1 comment "合同类型：1.一件代发，2.长期供货，3.框架合同，4.其他";

alter table zm_supplier.supplier_base add pay_type tinyint Unsigned DEFAULT 1 comment "付款方式：1.预付款，2.现付，3.账期";