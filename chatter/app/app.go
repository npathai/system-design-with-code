package app

type App struct {
	Svr *Server
}

func New() *App {
	return &App{}
}