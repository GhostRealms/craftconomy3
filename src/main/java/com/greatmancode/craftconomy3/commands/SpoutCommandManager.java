package com.greatmancode.craftconomy3.commands;

import org.spout.api.chat.ChatArguments;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandExecutor;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;
import org.spout.api.player.Player;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.SpoutLoader;

public class SpoutCommandManager implements CommandExecutor, CommandLoader {

	public SpoutCommandManager() {
		SpoutLoader.getInstance().getEngine().getRootCommand().addSubCommand(SpoutLoader.getInstance(), "money").setHelp("Money Related Commands").setExecutor(this);
	}

	@Override
	public boolean processCommand(CommandSource source, Command command, CommandContext args) throws CommandException {
		//TODO: Better way to handle no args.
		if (command.getPreferredName().equals("money")) {
			if (args.length() == 0) {

				if (Common.getInstance().getCommandManager().getMoneyCmdList().get("").playerOnly()) {
					if (!(source instanceof Player)) {
						source.sendMessage(ChatArguments.fromString("{{DARK_RED}}Only a player can use this command!"));
						return true;
					}
				}
				if (!(source instanceof Player) || Common.getInstance().getCommandManager().getMoneyCmdList().get("").permission(source.getName())) {
					Common.getInstance().getCommandManager().getMoneyCmdList().get("").execute(source.getName(), args.getRawArgs());
					return true;
				} else {
					source.sendMessage(ChatArguments.fromString("{{DARK_RED}}Not enough permissions!"));
					return true;
				}

			}
			if (Common.getInstance().getCommandManager().getMoneyCmdList().containsKey(args.getString(0))) {

				if (Common.getInstance().getCommandManager().getMoneyCmdList().get(args.getString(0)).playerOnly()) {
					if (!(source instanceof Player)) {
						source.sendMessage(ChatArguments.fromString("{{DARK_RED}}Only a player can use this command!"));
						return true;
					}
				}

				if (!(source instanceof Player) || Common.getInstance().getCommandManager().getMoneyCmdList().get(args.getString(0)).permission(source.getName())) {
					String[] newargs = new String[args.length() - 1];
					for (int i = 1; i < args.length(); i++) {
						newargs[i - 1] = args.getString(i);
					}
					if (newargs.length >= Common.getInstance().getCommandManager().getMoneyCmdList().get(args.getString(0)).minArgs() && newargs.length <= Common.getInstance().getCommandManager().getMoneyCmdList().get(args.getString(0)).maxArgs()) {
						source.sendMessage(Common.getInstance().getCommandManager().getMoneyCmdList().get(args.getString(0)).help());
						return true;
					}
					Common.getInstance().getCommandManager().getMoneyCmdList().get(args.getString(0)).execute(source.getName(), newargs);
					return true;
				} else {
					source.sendMessage(ChatArguments.fromString("{{DARK_RED}}Not enough permissions!"));
					return true;
				}
			}
		}
		return false;
	}
	
	

}
