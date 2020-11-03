package app

type AppOption func(*App)
type AppOptionCreator func() []AppOption

func ServerConnector(s *Server) AppOption {
	return func(app *App) {
		app.srv = s
	}
}