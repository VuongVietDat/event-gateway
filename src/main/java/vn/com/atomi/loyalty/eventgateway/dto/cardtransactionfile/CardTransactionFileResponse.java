package vn.com.atomi.loyalty.eventgateway.dto.cardtransactionfile;

public interface CardTransactionFileResponse {

  Long getId();

  String getCustomerName();

  Long getCardId();

  Long getCardNumber();

  String getCif();

  Long getProductId();

  String getCardRank();

  String getCardCategory();

  String getCardLimit();

  String getIssueOrganization();

  String getPhoneNumber();

  String getTotalTransaction();

  String getTotalAmount();

  String getMaturityDoubt();
}
