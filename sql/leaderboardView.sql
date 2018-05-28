CREATE OR REPLACE VIEW leaderboardView AS
    select username, count(*) as wins, (count(*) * 100 / (select count(*) from chess_game)) as winPercentage, cg.winner
        	from chess_game cg left outer join users on cg.winner = userid 
        	group by cg.winner, username
        	order by cg.winner;