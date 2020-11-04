package commands

import "github.com/spf13/cobra"

func Run(args []string) error {
	RootCmd.SetArgs(args)
	return RootCmd.Execute()
}

var RootCmd = &cobra.Command{
	Use: "chatter",
	Short: "A Mattermost clone",
	Long: "Offers workplace communication across all platforms",
}


