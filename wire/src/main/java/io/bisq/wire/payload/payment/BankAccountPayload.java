/*
 * This file is part of bisq.
 *
 * bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bisq.wire.payload.payment;

import io.bisq.common.app.Version;
import io.bisq.common.locale.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Locale;

public abstract class BankAccountPayload extends CountryBasedPaymentAccountPayload {
    // That object is sent over the wire, so we need to take care of version compatibility.
    private static final long serialVersionUID = Version.P2P_NETWORK_VERSION;

    private static final Logger log = LoggerFactory.getLogger(BankAccountPayload.class);

    protected String holderName;
    protected String bankName;
    protected String bankId;
    protected String branchId;
    protected String accountNr;
    protected String accountType;
    @Nullable
    protected String holderTaxId;

    public BankAccountPayload(String paymentMethod, String id, long maxTradePeriod) {
        super(paymentMethod, id, maxTradePeriod);
    }

    @Override
    public String getPaymentDetails(Locale locale) {
        return "Bank account transfer - " + getPaymentDetailsForTradePopup(locale).replace("\n", ", ");
    }

    @Override
    public String getPaymentDetailsForTradePopup(Locale locale) {
        String bankName = BankUtil.isBankNameRequired(countryCode) ? BankUtil.getBankNameLabel(countryCode) + " " + this.bankName + "\n" : "";
        String bankId = BankUtil.isBankIdRequired(countryCode) ? BankUtil.getBankIdLabel(countryCode) + " " + this.bankId + "\n" : "";
        String branchId = BankUtil.isBranchIdRequired(countryCode) ? BankUtil.getBranchIdLabel(countryCode) + " " + this.branchId + "\n" : "";
        String accountNr = BankUtil.isAccountNrRequired(countryCode) ? BankUtil.getAccountNrLabel(countryCode) + " " + this.accountNr + "\n" : "";
        String accountType = BankUtil.isAccountTypeRequired(countryCode) ? BankUtil.getAccountTypeLabel(countryCode) + " " + this.accountType + "\n" : "";
        String holderIdString = BankUtil.isHolderIdRequired(countryCode) ? (BankUtil.getHolderIdLabel(countryCode) + " " + holderTaxId + "\n") : "";

        return "Holder name: " + holderName + "\n" +
                bankName +
                bankId +
                branchId +
                accountNr +
                accountType +
                holderIdString +
                "Country of bank: " + new Locale(locale.getLanguage(), countryCode).getDisplayCountry();
    }


    protected String getHolderIdLabel() {
        return BankUtil.getHolderIdLabel(countryCode);
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Nullable
    public String getBankName() {
        return bankName;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    @Nullable
    public String getBankId() {
        return BankUtil.isBankIdRequired(countryCode) ? bankId : bankName;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    @Nullable
    public String getBranchId() {
        return branchId;
    }

    public void setAccountNr(String accountNr) {
        this.accountNr = accountNr;
    }

    @Nullable
    public String getAccountNr() {
        return accountNr;
    }

    public void setHolderTaxId(String holderTaxId) {
        this.holderTaxId = holderTaxId;
    }

    @Nullable
    public String getHolderTaxId() {
        return holderTaxId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Nullable
    public String getAccountType() {
        return accountType;
    }
}