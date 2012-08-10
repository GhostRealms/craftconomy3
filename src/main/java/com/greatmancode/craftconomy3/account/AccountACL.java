/*
 * This file is part of Craftconomy3.
 *
 * Copyright (c) 2011-2012, Greatman <http://github.com/greatman/>
 *
 * Craftconomy3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Craftconomy3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftconomy3.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.craftconomy3.account;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.database.tables.AccessTable;

/**
 * Used with bank accounts
 * @author greatman
 *
 */
public class AccountACL {

	//<Player Name, Access or not>
	private HashMap<String, AccountACLValue> aclList = new HashMap<String,AccountACLValue>();
	private Account account;
	public AccountACL(Account account, int accountID) {
		this.account = account;
		List<AccessTable> aclTable = Common.getInstance().getDatabaseManager().getDatabase().select(AccessTable.class).where().equal("account_id", accountID).execute().find();
		Iterator<AccessTable> aclIterator = aclTable.iterator();
		while(aclIterator.hasNext()) {
			AccessTable entry = aclIterator.next();
			aclList.put(entry.playerName, new AccountACLValue(entry));
		}
		
	}
	
	/**
	 * Checks if a player can deposit money
	 * @param name The player name
	 * @return True if the player can deposit money, else false
	 */
	public boolean canDeposit(String name) {
		boolean result = false;
		if (aclList.containsKey(name)) {
			result = aclList.get(name).getTable().deposit;
		}
		return result;
	}
	
	/**
	 * Checks if a player can withdraw money
	 * @param name The player name
	 * @return True if the player can withdraw money, else false.
	 */
	public boolean canWithdraw(String name) {
		boolean result = false;
		if (aclList.containsKey(name)) {
			result = aclList.get(name).getTable().withdraw;
		}
		return result;
	}
	
	/**
	 * Checks if a player can modify the ACL
	 * @param name The player name
	 * @return True if the player can modify the ACL, else false.
	 */
	public boolean canAcl(String name) {
		boolean result = false;
		if (aclList.containsKey(name)) {
			result = aclList.get(name).getTable().acl;
		}
		return result;
	}
	
	/**
	 * Checks if a player can show the balance of the account
	 * @param name The player name
	 * @return True if the player can show the balance of the account, else false.
	 */
	public boolean canShow(String name) {
		boolean result = false;
		if (aclList.containsKey(name)) {
			result = aclList.get(name).getTable().show;
		}
		return result;
	}
	
	/**
	 * Set if a player can deposit money in the account
	 * @param name The Player name
	 * @param deposit Can deposit or not
	 */
	public void setDeposit(String name, boolean deposit) {
		if (aclList.containsKey(name)) {
			AccountACLValue value = aclList.get(name);
			set(name, deposit, value.getTable().withdraw , value.getTable().acl, value.getTable().show);
		} else {
			set(name,deposit,false,false,false);
		}
	}
	
	/**
	 * Set if a player can withdraw money in the account
	 * @param name The Player name
	 * @param withdraw Can withdraw or not
	 */
	public void setWithdraw(String name, boolean withdraw) {
		if (aclList.containsKey(name)) {
			AccountACLValue value = aclList.get(name);
			set(name, value.getTable().deposit, withdraw, value.getTable().acl, value.getTable().show);
		} else {
			set(name,false,withdraw,false,false);
		}
	}
	
	/**
	 * Set if a player can modify the ACL list
	 * @param name The player name
	 * @param acl can modify the ACL or not
	 */
	public void setAcl(String name, boolean acl) {
		if (aclList.containsKey(name)) {
			AccountACLValue value = aclList.get(name);
			set(name, value.getTable().deposit, value.getTable().withdraw, acl, value.getTable().show);
		} else {
			set(name,false,false,acl,false);
		}
	}
	
	
	/**
	 * Set if a player can show the bank balance.
	 * @param name The player name
	 * @param show can show the bank balance or not.
	 */
	public void setShow(String name, boolean show) {
		if (aclList.containsKey(name)) {
			AccountACLValue value = aclList.get(name);
			set(name, value.getTable().deposit, value.getTable().withdraw, value.getTable().acl, show);
		} else {
			set(name,false,false,false,show);
		}
	}
	/**
	 * Set a player in the ACL list
	 * @param name The Player
	 * @param deposit Can deposit or not
	 * @param withdraw Can withdraw or not
	 * @param acl Can modify the ACL or not
	 * @param show Can show the balance
	 */
	public void set(String name, boolean deposit, boolean withdraw, boolean acl, boolean show) {
		AccessTable table = null;
		if (aclList.containsKey(name)) {
			table = Common.getInstance().getDatabaseManager().getDatabase().select(AccessTable.class).where().equal("id", aclList.get(name).getTable().id).execute().findOne();
		} else {
			table = new AccessTable();
		}
		
		table.account_id = getParent().getAccountID();
		table.playerName = name;
		table.deposit = deposit;
		table.withdraw = withdraw;
		table.acl = acl;
		table.show = show;
		Common.getInstance().getDatabaseManager().getDatabase().save(table);
		aclList.put(name, new AccountACLValue(table));
	}
	
	/**
	 * Returns the related account
	 * @return The related account
	 */
	public Account getParent() {
		return account;
	}
}
